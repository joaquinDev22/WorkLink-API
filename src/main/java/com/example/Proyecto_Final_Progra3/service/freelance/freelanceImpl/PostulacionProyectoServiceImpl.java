package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PostulacionProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.PostulacionProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.Proyecto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostulacionProyectoServiceImpl implements IPostulacionProyectoService {

    private final PostulacionProyectoRepository postulacionRepository;
    private final ProyectoRepository proyectoRepository;
    private final EmployeeRepository employeeRepository;
    private final PerfilRepository perfilRepository;

    @Override
    @Transactional
    public PostulacionProyectoResponseDTO postularseMe(String username, Integer proyectoId, PostulacionProyectoRequestDTO dto) {
        com.example.Proyecto_Final_Progra3.model.Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        Employee employee = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo los freelancers/empleados pueden postularse"));
                
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
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new RuntimeException("Empleado/Freelancer no encontrado"));

        PostulacionProyecto postulacion = new PostulacionProyecto();
        postulacion.setDescripcion(dto.descripcion());
        postulacion.setCosto(dto.costo());
        postulacion.setFechaPostulacion(LocalDate.now());
        postulacion.setEmployee(employee);
        postulacion.setProyecto(proyecto);

        PostulacionProyecto guardada = postulacionRepository.save(postulacion);
        return mapToDto(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostulacionProyectoResponseDTO> listarPostulacionesPorProyecto(Integer proyectoId) {
        return postulacionRepository.findByProyecto_Id(proyectoId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PostulacionProyectoResponseDTO mapToDto(PostulacionProyecto p) {
        return new PostulacionProyectoResponseDTO(
                p.getId(),
                p.getDescripcion(),
                p.getCosto(),
                p.getFechaPostulacion(),
                p.getEmployee().getEmployeeId(),
                p.getEmployee().getNombre() + " " + p.getEmployee().getApellido(),
                p.getProyecto().getId()
        );
    }
}
