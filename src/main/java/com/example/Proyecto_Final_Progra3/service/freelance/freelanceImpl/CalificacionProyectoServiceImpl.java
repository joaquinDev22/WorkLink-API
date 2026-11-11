package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.CalificacionProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.CalificacionProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ReputacionPerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.model.CalificacionUsuarioEntity;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.CalificacionProyectoEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import com.example.Proyecto_Final_Progra3.repository.CalificacionUsuarioRepository;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.CalificacionProyectoRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.ICalificacionProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalificacionProyectoServiceImpl implements ICalificacionProyectoService {

    private final CalificacionProyectoRepository calificacionProyectoRepository;
    private final CalificacionUsuarioRepository calificacionUsuarioRepository;
    private final ProyectoRepository proyectoRepository;
    private final PerfilRepository perfilRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public CalificacionProyectoResponseDTO calificarProyecto(Integer proyectoId, CalificacionProyectoRequestDTO dto, String username) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        boolean esCliente = proyecto.getClient() != null && 
                            proyecto.getClient().getPerfil() != null &&
                            proyecto.getClient().getPerfil().getUsuario().equals(username);
        boolean esFreelancer = proyecto.getFreelancer() != null && 
                               proyecto.getFreelancer().getPerfil() != null &&
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
        PerfilEntity perfilDestino = null;
        if (esCliente && proyecto.getFreelancer() != null) {
            perfilDestino = proyecto.getFreelancer().getPerfil();
        } else if (esFreelancer && proyecto.getClient() != null) {
            perfilDestino = proyecto.getClient().getPerfil();
        }

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
    public ReputacionPerfilResponseDTO obtenerReputacionPorUsuario(String username) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
        return obtenerReputacionPerfil(perfil.getId());
    }

    @Override
    public ReputacionPerfilResponseDTO obtenerReputacionPerfil(Integer perfilId) {
        PerfilEntity perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado con ID: " + perfilId));

        List<CalificacionUsuarioEntity> registros = calificacionUsuarioRepository.findByPerfil_Id(perfilId);

        List<CalificacionProyectoResponseDTO> resenas = new ArrayList<>(registros.stream()
                .filter(r -> r.getCalificacion() != null)
                .map(r -> mapToDto(r.getCalificacion()))
                .toList());

        // Si no se encontraron reseñas en calificacion_usuario, buscar calificaciones de proyectos asignados
        if (resenas.isEmpty()) {
            EmployeeEntity employee = employeeRepository.findByPerfil(perfil).orElse(null);
            if (employee != null) {
                List<CalificacionProyectoEntity> califsProyecto = calificacionProyectoRepository.findByFreelancerEmployeeId(employee.getEmployeeId());
                for (CalificacionProyectoEntity cp : califsProyecto) {
                    resenas.add(mapToDto(cp));
                }
            }
        }

        double promedio = resenas.isEmpty() ? 0.0 : 
                resenas.stream()
                        .filter(r -> r != null && r.puntaje() != null)
                        .mapToInt(r -> r.puntaje())
                        .average()
                        .orElse(0.0);

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
                entity.getTituloResena(),
                entity.getPuntaje(),
                entity.getComentario(),
                entity.getFechaComentario(),
                entity.getProyecto() != null ? entity.getProyecto().getId() : null
        );
    }
}

