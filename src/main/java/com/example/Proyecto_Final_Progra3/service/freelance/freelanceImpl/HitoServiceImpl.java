package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.HitoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.HitoResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoHitoProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.HitoProyectoEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import com.example.Proyecto_Final_Progra3.repository.freelance.HitoProyectoRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IHitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitoServiceImpl implements IHitoService {

    private final HitoProyectoRepository hitoRepository;
    private final ProyectoRepository proyectoRepository;

    @Override
    @Transactional
    public HitoResponseDTO crearHito(Integer proyectoId, HitoRequestDTO dto, String username) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        if (!proyecto.getClient().getPerfil().getUsuario().equals(username)) {
            throw new UnauthorizedAccessException("Solo el cliente propietario puede crear hitos en este proyecto");
        }

        HitoProyectoEntity hito = HitoProyectoEntity.builder()
                .titulo(dto.titulo())
                .descripcion(dto.descripcion())
                .monto(dto.monto())
                .estado(EstadoHitoProyecto.PENDIENTE)
                .fecha(dto.fechaLimite())
                .proyecto(proyecto)
                .build();

        HitoProyectoEntity guardado = hitoRepository.save(hito);
        return mapToDto(guardado);
    }

    @Override
    public List<HitoResponseDTO> listarHitosPorProyecto(Integer proyectoId) {
        return hitoRepository.findByProyecto_Id(proyectoId).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public HitoResponseDTO cambiarEstadoHito(Integer proyectoId, Integer hitoId, EstadoHitoProyecto nuevoEstado, String username) {
        HitoProyectoEntity hito = hitoRepository.findById(hitoId)
                .orElseThrow(() -> new ResourceNotFoundException("Hito no encontrado con ID: " + hitoId));

        if (!hito.getProyecto().getId().equals(proyectoId)) {
            throw new BusinessRuleException("El hito no pertenece al proyecto indicado");
        }

        ProyectoEntity proyecto = hito.getProyecto();
        boolean esCliente = proyecto.getClient().getPerfil().getUsuario().equals(username);
        boolean esFreelancer = proyecto.getFreelancer() != null && 
                               proyecto.getFreelancer().getPerfil().getUsuario().equals(username);

        if (!esCliente && !esFreelancer) {
            throw new UnauthorizedAccessException("No tienes permisos para modificar hitos de este proyecto");
        }

        if (nuevoEstado == EstadoHitoProyecto.PAGADO && !esCliente) {
            throw new UnauthorizedAccessException("Solo el cliente titular puede marcar un hito como pagado");
        }

        hito.setEstado(nuevoEstado);
        HitoProyectoEntity actualizado = hitoRepository.save(hito);
        return mapToDto(actualizado);
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

