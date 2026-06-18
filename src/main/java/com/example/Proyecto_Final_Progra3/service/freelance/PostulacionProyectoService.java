package com.example.Proyecto_Final_Progra3.service.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PostulacionProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.PostulacionProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.Proyecto;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.PostulacionProyectoRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostulacionProyectoService {

    private final PostulacionProyectoRepository postulacionRepository;
    private final ProyectoRepository proyectoRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public PostulacionProyectoResponseDTO postularse(Integer proyectoId, PostulacionProyectoRequestDTO dto) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Empleado/Freelancer no encontrado"));

        PostulacionProyecto postulacion = new PostulacionProyecto();
        postulacion.setDescripcion(dto.getDescripcion());
        postulacion.setCosto(dto.getCosto());
        postulacion.setFechaPostulacion(LocalDate.now());
        postulacion.setEmployee(employee);
        postulacion.setProyecto(proyecto);

        PostulacionProyecto guardada = postulacionRepository.save(postulacion);
        return mapToDto(guardada);
    }

    @Transactional(readOnly = true)
    public List<PostulacionProyectoResponseDTO> listarPostulacionesPorProyecto(Integer proyectoId) {
        return postulacionRepository.findByProyecto_Id(proyectoId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PostulacionProyectoResponseDTO mapToDto(PostulacionProyecto p) {
        PostulacionProyectoResponseDTO dto = new PostulacionProyectoResponseDTO();
        dto.setPostulacionId(p.getId());
        dto.setDescripcion(p.getDescripcion());
        dto.setCostoPropuesto(p.getCosto());
        dto.setFechaPostulacion(p.getFechaPostulacion());
        dto.setEmpleadoId(p.getEmployee().getEmployeeId());
        dto.setNombreEmpleado(p.getEmployee().getNombre() + " " + p.getEmployee().getApellido());
        dto.setProyectoId(p.getProyecto().getId());
        return dto;
    }
}
