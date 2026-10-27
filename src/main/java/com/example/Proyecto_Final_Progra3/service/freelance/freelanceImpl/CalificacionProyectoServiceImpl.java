package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.CalificacionProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.CalificacionProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ReputacionPerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.model.CalificacionUsuarioEntity;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.CalificacionProyectoEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import com.example.Proyecto_Final_Progra3.repository.CalificacionUsuarioRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.CalificacionProyectoRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.ICalificacionProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalificacionProyectoServiceImpl implements ICalificacionProyectoService {

    private final CalificacionProyectoRepository calificacionProyectoRepository;
    private final CalificacionUsuarioRepository calificacionUsuarioRepository;
    private final ProyectoRepository proyectoRepository;
    private final PerfilRepository perfilRepository;

    @Override
    @Transactional
    public CalificacionProyectoResponseDTO calificarProyecto(Integer proyectoId, CalificacionProyectoRequestDTO dto, String username) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        boolean esCliente = proyecto.getClient() != null && 
                            proyecto.getClient().getPerfil().getUsuario().equals(username);
        boolean esFreelancer = proyecto.getFreelancer() != null && 
                               proyecto.getFreelancer().getPerfil().getUsuario().equals(username);

        if (!esCliente && !esFreelancer) {
            throw new UnauthorizedAccessException("Solo el cliente o el freelancer participante pueden calificar este proyecto");
        }

        // Crear registro en calificacion_proyecto
        CalificacionProyectoEntity calificacion = CalificacionProyectoEntity.builder()
                .puntaje(dto.puntaje())
                .comentario(dto.comentario())
                .fechaComentario(LocalDate.now())
                .proyecto(proyecto)
                .build();

        CalificacionProyectoEntity guardada = calificacionProyectoRepository.save(calificacion);

        // Asociar la calificación a la reputación del otro participante (calificacion_usuario)
        PerfilEntity perfilDestino = esCliente 
                ? proyecto.getFreelancer().getPerfil() 
                : proyecto.getClient().getPerfil();

        if (perfilDestino != null) {
            CalificacionUsuarioEntity calificacionUsuario = CalificacionUsuarioEntity.builder()
                    .calificacion(guardada)
                    .perfil(perfilDestino)
                    .build();
            calificacionUsuarioRepository.save(calificacionUsuario);
        }

        return mapToDto(guardada);
    }

    @Override
    public List<CalificacionProyectoResponseDTO> listarCalificacionesPorProyecto(Integer proyectoId) {
        return calificacionProyectoRepository.findByProyecto_Id(proyectoId).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ReputacionPerfilResponseDTO obtenerReputacionPerfil(Integer perfilId) {
        if (!perfilRepository.existsById(perfilId)) {
            throw new ResourceNotFoundException("Perfil no encontrado con ID: " + perfilId);
        }

        List<CalificacionUsuarioEntity> registros = calificacionUsuarioRepository.findByPerfil_Id(perfilId);

        List<CalificacionProyectoResponseDTO> resenas = registros.stream()
                .filter(r -> r.getCalificacion() != null)
                .map(r -> mapToDto(r.getCalificacion()))
                .toList();

        double promedio = resenas.isEmpty() ? 0.0 : 
                resenas.stream().mapToInt(CalificacionProyectoResponseDTO::puntaje).average().orElse(0.0);

        // Redondear a 1 decimal
        double promedioRedondeado = Math.round(promedio * 10.0) / 10.0;

        return new ReputacionPerfilResponseDTO(
                perfilId,
                promedioRedondeado,
                resenas.size(),
                resenas
        );
    }

    private CalificacionProyectoResponseDTO mapToDto(CalificacionProyectoEntity entity) {
        return new CalificacionProyectoResponseDTO(
                entity.getCalificacionId(),
                entity.getPuntaje(),
                entity.getComentario(),
                entity.getFechaComentario(),
                entity.getProyecto() != null ? entity.getProyecto().getId() : null
        );
    }
}

