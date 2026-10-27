package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PostulacionProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.mappers.PostulacionProyectoMapper;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.PostulacionProyectoEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.PostulacionProyectoRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IPostulacionProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostulacionProyectoServiceImpl implements IPostulacionProyectoService {

    private final PostulacionProyectoRepository postulacionRepository;
    private final ProyectoRepository proyectoRepository;
    private final EmployeeRepository employeeRepository;
    private final PerfilRepository perfilRepository;
    private final PostulacionProyectoMapper postulacionProyectoMapper;

    @Override
    @Transactional
    public PostulacionProyectoResponseDTO postularseMe(String username, Integer proyectoId, PostulacionProyectoRequestDTO dto) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        EmployeeEntity employee = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("Solo los freelancers/empleados pueden postularse"));
                
        PostulacionProyectoRequestDTO dtoConEmployee = new PostulacionProyectoRequestDTO(
                dto.descripcion(),
                dto.costo(),
                employee.getEmployeeId()
        );
        return postularse(proyectoId, dtoConEmployee);
    }

    @Override
    @Transactional
    public PostulacionProyectoResponseDTO postularse(Integer proyectoId, PostulacionProyectoRequestDTO dto) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        EmployeeEntity employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado/Freelancer no encontrado con ID: " + dto.employeeId()));

        PostulacionProyectoEntity postulacion = new PostulacionProyectoEntity();
        postulacion.setDescripcion(dto.descripcion());
        postulacion.setCosto(dto.costo());
        postulacion.setFechaPostulacion(LocalDate.now());
        postulacion.setEmployee(employee);
        postulacion.setProyecto(proyecto);

        PostulacionProyectoEntity guardada = postulacionRepository.save(postulacion);
        return postulacionProyectoMapper.toDto(guardada);
    }

    @Override
    public List<PostulacionProyectoResponseDTO> listarPostulacionesPorProyecto(Integer proyectoId, String username) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        boolean esClienteTitular = proyecto.getClient() != null && 
                                   proyecto.getClient().getPerfil().getUsuario().equals(username);

        List<PostulacionProyectoEntity> postulaciones = postulacionRepository.findByProyecto_Id(proyectoId);

        // Control de Acceso: El cliente titular ve todas las propuestas
        if (esClienteTitular) {
            return postulacionProyectoMapper.toDtoList(postulaciones);
        }

        // Si es un freelancer, solo puede ver su propia propuesta para evitar espionaje de tarifas
        return postulaciones.stream()
                .filter(p -> p.getEmployee() != null && 
                             p.getEmployee().getPerfil() != null && 
                             p.getEmployee().getPerfil().getUsuario().equals(username))
                .map(postulacionProyectoMapper::toDto)
                .toList();
    }
}
