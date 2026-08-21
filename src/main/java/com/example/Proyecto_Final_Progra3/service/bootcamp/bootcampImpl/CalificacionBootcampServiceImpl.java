package com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.CalificacionBootcampRequestDTO;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.CalificacionBootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Inscripcion;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.EstadoInscripcion;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.CalificacionBootcampRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InscripcionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import org.springframework.transaction.annotation.Transactional;
import com.example.Proyecto_Final_Progra3.dto.responsedto.CalificacionBootcampResponseDTO;
import com.example.Proyecto_Final_Progra3.mappers.CalificacionBootcampMapper;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class CalificacionBootcampService {
    private final CalificacionBootcampRepository calificacionBootcampRepository;
    private final BootcampRepository bootcampRepository;
    private final EmployeeRepository employeeRepository;
    private final InscripcionRepository inscripcionBootcampRepository;
    private final CalificacionBootcampMapper mapper;

    @Transactional
    public CalificacionBootcampResponseDTO calificarBootcamp(Integer bootcampId, CalificacionBootcampRequestDTO dto) {

        validarCalificacion(dto);

        Bootcamp bootcamp = bootcampRepository.findById(bootcampId)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp con ese ID"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("No existe un empleado con ese ID"));

        Inscripcion inscripcion = inscripcionBootcampRepository
                .findByBootcampAndEmployee(bootcamp, employee)
                .orElseThrow(() -> new RuntimeException("El empleado no está inscripto en este bootcamp"));

        if (inscripcion.getEstado() == EstadoInscripcion.CANCELADO) {
            throw new RuntimeException("No se puede calificar un bootcamp con inscripción cancelada");
        }

        boolean yaCalifico = calificacionBootcampRepository.existsByBootcampAndEmployee(bootcamp, employee);

        if (yaCalifico) {
            throw new RuntimeException("El empleado ya calificó este bootcamp");
        }

        CalificacionBootcamp calificacion = new CalificacionBootcamp();
        calificacion.setBootcamp(bootcamp);
        calificacion.setEmployee(employee);
        calificacion.setPuntaje(dto.getPuntaje());
        calificacion.setComentario(dto.getComentario());
        calificacion.setFechaComentario(LocalDate.now());

        calificacion = calificacionBootcampRepository.save(calificacion);
        return mapper.toDto(calificacion);
    }

    private void validarCalificacion(CalificacionBootcampRequestDTO dto) {

        if (dto.getEmployeeId() == null) {
            throw new RuntimeException("El empleado es obligatorio");
        }

        if (dto.getPuntaje() == null) {
            throw new RuntimeException("El puntaje es obligatorio");
        }

        if (dto.getPuntaje() < 1 || dto.getPuntaje() > 5) {
            throw new RuntimeException("El puntaje debe estar entre 1 y 5");
        }

        if (dto.getComentario() == null || dto.getComentario().isBlank()) {
            throw new RuntimeException("El comentario es obligatorio");
        }

        if (dto.getComentario().length() > 255) {
            throw new RuntimeException("El comentario no puede superar los 255 caracteres");
        }
    }
}
