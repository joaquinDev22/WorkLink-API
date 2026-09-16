package com.example.Proyecto_Final_Progra3.freelance.proyecto;

import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.errors.UnauthorizedAccessException;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;
import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilRepository;

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
    public List<PostulacionProyectoResponseDTO> listarPostulacionesPorProyecto(Integer proyectoId) {
        return postulacionProyectoMapper.toDtoList(postulacionRepository.findByProyecto_Id(proyectoId));
    }
}
