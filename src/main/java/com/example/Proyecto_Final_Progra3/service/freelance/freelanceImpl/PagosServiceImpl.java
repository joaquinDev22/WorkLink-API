package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PagosRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.MercadoPagoPreferenceResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.mappers.PagosMapper;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.Moneda;
import com.example.Proyecto_Final_Progra3.model.freelance.pagos.PagosEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import com.example.Proyecto_Final_Progra3.repository.freelance.PagosRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IPagosService;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PagosServiceImpl implements IPagosService {

    private final PagosRepository pagosRepository;
    private final ProyectoRepository proyectoRepository;
    private final PagosMapper pagosMapper;
    private final MercadoPagoAdapter mercadoPagoAdapter;

    @Override
    @Transactional
    public PagosResponseDTO registrarPago(Integer proyectoId, PagosRequestDTO dto) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        PagosEntity pago = new PagosEntity();
        pago.setMoneda(dto.moneda());
        pago.setMonto(dto.monto());
        pago.setMedioDePago(dto.medioDePago());
        pago.setProyecto(proyecto);

        PagosEntity guardado = pagosRepository.save(pago);
        return pagosMapper.toDto(guardado);
    }

    @Override
    public List<PagosResponseDTO> listarPagos(Integer proyectoId) {
        return pagosMapper.toDtoList(pagosRepository.findByProyecto_Id(proyectoId));
    }

    @Override
    public MercadoPagoPreferenceResponseDTO crearPreferenciaPago(Integer proyectoId, BigDecimal monto, String backUrl) {
        // Consulta rápida de solo lectura para validar existencia del proyecto
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        BigDecimal montoFinal = (monto != null && monto.compareTo(BigDecimal.ZERO) > 0)
                ? monto
                : (proyecto.getPresupuestoMax() != null ? proyecto.getPresupuestoMax() : BigDecimal.valueOf(1000));

        String titulo = "Pago proyecto #" + proyectoId + ": " + (proyecto.getArea() != null ? proyecto.getArea() : "Servicio Freelance");

        // Llamada externa sin transacción abierta (protección del connection pool)
        return mercadoPagoAdapter.crearPreferencia(proyectoId, titulo, montoFinal, backUrl);
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
                    String externalRef = payment.getExternalReference();
                    if (externalRef != null && !externalRef.isBlank()) {
                        Integer proyectoId = Integer.valueOf(externalRef);
                        guardarPagoConfirmado(proyectoId, payment);
                    }
                } else {
                    log.info("Pago {} en Mercado Pago recibido con estado: {}", paymentId, payment.getStatus());
                }
            } catch (Exception e) {
                log.error("Error al procesar webhook de Mercado Pago para id={}: {}", paymentIdStr, e.getMessage(), e);
            }
        }
    }

    @Transactional
    public void guardarPagoConfirmado(Integer proyectoId, Payment payment) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId).orElse(null);
        if (proyecto == null) {
            log.warn("Proyecto no encontrado para externalReference={} al confirmar pago en Mercado Pago", proyectoId);
            return;
        }

        Integer monto = payment.getTransactionAmount() != null ? payment.getTransactionAmount().intValue() : 0;

        PagosEntity pago = new PagosEntity();
        pago.setMoneda(Moneda.PESOS);
        pago.setMonto(monto);
        pago.setMedioDePago(MedioDePago.BILLETERA_VIRTUAL);
        pago.setProyecto(proyecto);

        pagosRepository.save(pago);
        log.info("Pago de Mercado Pago guardado exitosamente para el proyecto ID: {}", proyectoId);
    }
}
