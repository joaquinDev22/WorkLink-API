package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.HitoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.HitoResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoHitoProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoPostulacionProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.HitoProyectoEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import com.example.Proyecto_Final_Progra3.repository.freelance.HitoProyectoRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.PostulacionProyectoRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IHitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitoServiceImpl implements IHitoService {

    private final HitoProyectoRepository hitoRepository;
    private final ProyectoRepository proyectoRepository;
    private final PostulacionProyectoRepository postulacionProyectoRepository;

    @Override
    @Transactional
    public HitoResponseDTO crearHito(Integer proyectoId, HitoRequestDTO dto, String username) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        if (proyecto.getClient() == null || proyecto.getClient().getPerfil() == null || !proyecto.getClient().getPerfil().getUsuario().equals(username)) {
            throw new UnauthorizedAccessException("Solo el cliente propietario puede crear hitos en este proyecto");
        }
        // Los hitos se crean después de adjudicar: antes no hay monto acordado que repartir
        if (proyecto.getEstado() != EstadoProyecto.EN_PROGRESO) {
            throw new BusinessRuleException("Solo se pueden crear hitos en proyectos adjudicados y en progreso (estado actual: " + proyecto.getEstado() + ")");
        }

        HitoProyectoEntity hito = HitoProyectoEntity.builder()
                .titulo(dto.titulo())
                .descripcion(dto.descripcion())
                .monto(BigDecimal.ZERO)            // lo calcula repartirMontoAcordado
                .estado(EstadoHitoProyecto.PENDIENTE)
                .fecha(dto.fechaLimite())
                .proyecto(proyecto)
                .build();
        hitoRepository.save(hito);
        repartirMontoAcordado(proyecto);
        return mapToDto(hito);
    }

    @Override
    public List<HitoResponseDTO> listarHitosPorProyecto(Integer proyectoId, String username) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));
        boolean esCliente = proyecto.getClient() != null && proyecto.getClient().getPerfil() != null
                && username.equals(proyecto.getClient().getPerfil().getUsuario());
        boolean esFreelancer = proyecto.getFreelancer() != null && proyecto.getFreelancer().getPerfil() != null
                && username.equals(proyecto.getFreelancer().getPerfil().getUsuario());
        if (!esCliente && !esFreelancer) {
            throw new UnauthorizedAccessException("Solo el cliente o el freelancer del proyecto pueden ver sus hitos");
        }
        return hitoRepository.findByProyecto_IdOrderByHitoIdAsc(proyectoId).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public HitoResponseDTO cambiarEstadoHito(Integer proyectoId, Integer hitoId, EstadoHitoProyecto nuevoEstado, String username) {
        HitoProyectoEntity hito = hitoRepository.findById(hitoId)
                .orElseThrow(() -> new ResourceNotFoundException("Hito no encontrado con ID: " + hitoId));

        ProyectoEntity proyecto = hito.getProyecto();
        if (proyecto == null || !proyecto.getId().equals(proyectoId)) {
            throw new BusinessRuleException("El hito no pertenece al proyecto indicado");
        }

        boolean esCliente = proyecto.getClient() != null &&
        proyecto.getClient().getPerfil() != null && 
        username.equals(proyecto.getClient().getPerfil().getUsuario());

        boolean esFreelancer = proyecto.getFreelancer() != null &&
        proyecto.getFreelancer().getPerfil() != null &&
        username.equals(proyecto.getFreelancer().getPerfil().getUsuario());

        if (!esCliente && !esFreelancer) {
            throw new UnauthorizedAccessException("No tienes permisos para modificar hitos de este proyecto");
        }

        // D2: EN_PROGRESO solo lo pone el webhook al confirmarse el pago; a mano solo se puede completar
        if (nuevoEstado != EstadoHitoProyecto.COMPLETADO) {
            throw new BusinessRuleException("El hito pasa a EN_PROGRESO solo cuando Mercado Pago confirma el pago; manualmente solo se puede marcar como COMPLETADO");
        }
        if (!esFreelancer) {
            throw new UnauthorizedAccessException("Solo el freelancer asignado puede marcar el hito como completado");
        }
        if (hito.getEstado() != EstadoHitoProyecto.EN_PROGRESO) {
            throw new BusinessRuleException("Solo se puede completar un hito pagado y en progreso (estado actual: " + hito.getEstado() + ")");
        }

        hito.setEstado(EstadoHitoProyecto.COMPLETADO);
        HitoProyectoEntity actualizado = hitoRepository.save(hito);

        // D5: cuando se completa el último hito, el proyecto se finaliza solo
        if (hitoRepository.findByProyecto_Id(proyectoId).stream().allMatch(h -> h.getEstado() == EstadoHitoProyecto.COMPLETADO)) {
            proyecto.setEstado(EstadoProyecto.FINALIZADO);
            proyectoRepository.save(proyecto);
        }
        return mapToDto(actualizado);
    }

    // D3: lo que falta pagar del monto acordado se reparte en partes iguales entre los hitos sin pagar
    private void repartirMontoAcordado(ProyectoEntity proyecto) {
        BigDecimal acordado = postulacionProyectoRepository
                .findByProyecto_IdAndEstado(proyecto.getId(), EstadoPostulacionProyecto.ACEPTADA)
                .orElseThrow(() -> new BusinessRuleException("El proyecto no tiene una propuesta aceptada; adjudicalo antes de crear hitos"))
                .getCosto();
        List<HitoProyectoEntity> hitos = hitoRepository.findByProyecto_IdOrderByHitoIdAsc(proyecto.getId());
        BigDecimal yaPagado = hitos.stream()
                .filter(h -> h.getEstado() != EstadoHitoProyecto.PENDIENTE)
                .map(hito -> hito.getMonto())
                .reduce(BigDecimal.ZERO, (acumulado, monto) -> acumulado.add(monto));
        List<HitoProyectoEntity> pendientes = hitos.stream()
                .filter(h -> h.getEstado() == EstadoHitoProyecto.PENDIENTE)
                .toList();
        BigDecimal restante = acordado.subtract(yaPagado);
        if (restante.signum() <= 0) {
            throw new BusinessRuleException("Ya se pagó el total acordado ($" + acordado + "); no se pueden agregar más hitos");
        }
        BigDecimal parte = restante.divide(BigDecimal.valueOf(pendientes.size()), 2, RoundingMode.DOWN);
        for (int i = 0; i < pendientes.size(); i++) {
            boolean ultimo = i == pendientes.size() - 1;
            // El último hito absorbe los centavos del redondeo: 2000 / 3 = 666,66 + 666,66 + 666,68
            pendientes.get(i).setMonto(ultimo ? restante.subtract(parte.multiply(BigDecimal.valueOf(i))) : parte);
        }
        hitoRepository.saveAll(pendientes);
    }

    private HitoResponseDTO mapToDto(HitoProyectoEntity entity) {
        return new HitoResponseDTO(
                entity.getHitoId(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getMonto(),
                entity.getEstado() != null ? entity.getEstado().name() : EstadoHitoProyecto.PENDIENTE.name(),
                entity.getFecha(),
                entity.getProyecto() != null ? entity.getProyecto().getId() : null
        );
    }
}
