package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.responsedto.MercadoPagoPreferenceResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;
import com.example.Proyecto_Final_Progra3.events.PagoConfirmadoEvent;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.mappers.PagosMapper;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoHitoProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.Moneda;
import com.example.Proyecto_Final_Progra3.model.freelance.pagos.PagosEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.HitoProyectoEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import com.example.Proyecto_Final_Progra3.repository.freelance.HitoProyectoRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.PagosRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IPagosService;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

// D1: el pago directo se eliminó; el paquete strategy queda en desuso (se conserva como ejemplo del patrón)
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PagosServiceImpl implements IPagosService {

    private final PagosRepository pagosRepository;
    private final ProyectoRepository proyectoRepository;
    private final HitoProyectoRepository hitoRepository;
    private final PagosMapper pagosMapper;
    private final MercadoPagoAdapter mercadoPagoAdapter;
    private final ApplicationEventPublisher eventPublisher;

    @org.springframework.beans.factory.annotation.Autowired
    @org.springframework.context.annotation.Lazy
    private PagosServiceImpl self;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public List<PagosResponseDTO> listarPagos(Integer proyectoId, String username) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));
        boolean esCliente = esUsuario(proyecto.getClient() != null ? proyecto.getClient().getPerfil() : null, username);
        boolean esFreelancer = esUsuario(proyecto.getFreelancer() != null ? proyecto.getFreelancer().getPerfil() : null, username);
        if (!esCliente && !esFreelancer) {
            throw new UnauthorizedAccessException("Solo el cliente o el freelancer del proyecto pueden ver sus pagos");
        }
        return pagosMapper.toDtoList(pagosRepository.findByProyecto_Id(proyectoId));
    }

    @Override
    public MercadoPagoPreferenceResponseDTO crearPreferenciaPago(Integer proyectoId, Integer hitoId, String username) {
        HitoProyectoEntity hito = hitoRepository.findById(hitoId)
                .orElseThrow(() -> new ResourceNotFoundException("Hito no encontrado con ID: " + hitoId));
        ProyectoEntity proyecto = hito.getProyecto();
        if (!proyecto.getId().equals(proyectoId)) {
            throw new BusinessRuleException("El hito " + hitoId + " no pertenece al proyecto " + proyectoId);
        }
        if (!esUsuario(proyecto.getClient() != null ? proyecto.getClient().getPerfil() : null, username)) {
            throw new UnauthorizedAccessException("Solo el cliente titular del proyecto puede pagar sus hitos");
        }
        if (proyecto.getEstado() != EstadoProyecto.EN_PROGRESO) {
            throw new BusinessRuleException("Solo se pagan hitos de proyectos en progreso (estado actual: " + proyecto.getEstado() + ")");
        }
        // D5: un hito por vez y en orden; el próximo es el primero que no está completado
        HitoProyectoEntity siguiente = hitoRepository.findByProyecto_IdOrderByHitoIdAsc(proyectoId).stream()
                .filter(h -> h.getEstado() != EstadoHitoProyecto.COMPLETADO)
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("Todos los hitos del proyecto ya están completados"));
        if (!siguiente.getHitoId().equals(hitoId) || siguiente.getEstado() != EstadoHitoProyecto.PENDIENTE) {
            throw new BusinessRuleException("Solo se puede pagar el próximo hito pendiente (#" + siguiente.getHitoId()
                    + ", estado " + siguiente.getEstado() + "), y solo cuando el anterior esté completado");
        }
        String titulo = "Hito #" + hitoId + ": " + hito.getTitulo();
        // external_reference "{proyectoId}-{hitoId}": el webhook sabe qué hito habilitar y el front saca el proyecto del primer número
        return mercadoPagoAdapter.crearPreferencia(proyectoId + "-" + hitoId, titulo, hito.getMonto(), frontendUrl + "/freelance");
    }

    @Override
    public void procesarWebhook(String topic, String id, String type, String dataId) {
        String paymentIdStr = (dataId != null && !dataId.isBlank()) ? dataId : id;
        String eventType = (type != null && !type.isBlank()) ? type : topic;

        if (paymentIdStr == null || paymentIdStr.isBlank()) {
            log.warn("Webhook de Mercado Pago recibido sin ID de recurso: type={}, topic={}", type, topic);
            return;
        }

        if ("payment".equalsIgnoreCase(eventType)) {
            try {
                Long paymentId = Long.valueOf(paymentIdStr);
                // Consulta HTTP externa hacia Mercado Pago fuera de transacción de base de datos
                Payment payment = mercadoPagoAdapter.consultarPago(paymentId);

                if ("approved".equalsIgnoreCase(payment.getStatus())) {
                    String[] ref = payment.getExternalReference() != null ? payment.getExternalReference().split("-") : new String[0];
                    if (ref.length != 2) {
                        // preferencias viejas (solo proyectoId) o ajenas: no hay hito que habilitar
                        log.warn("Pago {} aprobado con external_reference '{}' sin formato proyecto-hito: se ignora", paymentId, payment.getExternalReference());
                        return;
                    }
                    (self != null ? self : this).guardarPagoConfirmado(Integer.valueOf(ref[1]), payment);
                } else {
                    log.info("Pago {} en Mercado Pago recibido con estado: {}", paymentId, payment.getStatus());
                }
            } catch (NumberFormatException e) {
                // ids no numéricos: la notificación no es nuestra; se responde 200 para que Mercado Pago no reintente.
                // El resto de errores se propaga (5xx/422) para que Mercado Pago reintente y el pago no se pierda.
                log.warn("Webhook de Mercado Pago con identificadores no numéricos: data.id={}", paymentIdStr);
            }
        }
    }

    @Transactional
    public void guardarPagoConfirmado(Integer hitoId, Payment payment) {
        String mpId = String.valueOf(payment.getId());
        if (pagosRepository.existsByMpPaymentId(mpId)) {
            log.info("Notificación repetida de Mercado Pago para el pago {}: ya estaba registrado, se ignora", mpId);
            return;
        }
        HitoProyectoEntity hito = hitoRepository.findById(hitoId).orElse(null);
        if (hito == null) {
            log.warn("Pago {} aprobado para el hito {}, que no existe: se ignora", mpId, hitoId);
            return;
        }
        BigDecimal monto = payment.getTransactionAmount() != null ? payment.getTransactionAmount() : BigDecimal.ZERO;

        PagosEntity pago = new PagosEntity();
        pago.setMoneda(Moneda.PESOS);
        pago.setMonto(monto);
        pago.setMedioDePago(MedioDePago.BILLETERA_VIRTUAL);
        pago.setProyecto(hito.getProyecto());
        pago.setHito(hito);
        pago.setMpPaymentId(mpId);
        PagosEntity guardado = pagosRepository.save(pago);   // el dinero se cobró: se registra siempre

        // D2/D4: solo un pago completo de un hito pendiente lo habilita
        if (hito.getEstado() == EstadoHitoProyecto.PENDIENTE && monto.compareTo(hito.getMonto()) >= 0) {
            hito.setEstado(EstadoHitoProyecto.EN_PROGRESO);
            hitoRepository.save(hito);
        } else {
            log.warn("Pago {} de ${} registrado para el hito {} ({}, monto ${}): no cambia el estado, revisar a mano",
                    mpId, monto, hitoId, hito.getEstado(), hito.getMonto());
        }

        eventPublisher.publishEvent(new PagoConfirmadoEvent(
                guardado.getPagosId(), hito.getProyecto().getId(), guardado.getMonto(), "PESOS", "BILLETERA_VIRTUAL"));
    }

    private static boolean esUsuario(PerfilEntity perfil, String username) {
        return perfil != null && perfil.getUsuario().equals(username);
    }
}
