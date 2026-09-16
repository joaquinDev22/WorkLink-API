package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import com.example.Proyecto_Final_Progra3.errors.BadRequestException;
import com.example.Proyecto_Final_Progra3.errors.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class CalificacionBootcampServiceImpl implements ICalificacionBootcampService {
    private final CalificacionBootcampRepository calificacionBootcampRepository;
    private final BootcampRepository bootcampRepository;
    private final EmployeeRepository employeeRepository;
    private final InscripcionRepository inscripcionBootcampRepository;
    private final CalificacionBootcampMapper mapper;

    @Transactional
    public CalificacionBootcampResponseDTO calificarBootcamp(Integer bootcampId, CalificacionBootcampRequestDTO dto) {

        validarCalificacion(dto);

        BootcampEntity bootcamp = bootcampRepository.findById(bootcampId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un bootcamp con el ID: " + bootcampId));

        EmployeeEntity employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un empleado con el ID: " + dto.employeeId()));

        InscripcionEntity inscripcion = inscripcionBootcampRepository
                .findByBootcampAndEmployee(bootcamp, employee)
                .orElseThrow(() -> new BusinessRuleException("El empleado no está inscripto en este bootcamp"));

        if (inscripcion.getEstado() == EstadoInscripcion.CANCELADO) {
            throw new BusinessRuleException("No se puede calificar un bootcamp con inscripción cancelada");
        }

        boolean yaCalifico = calificacionBootcampRepository.existsByBootcampAndEmployee(bootcamp, employee);

        if (yaCalifico) {
            throw new BusinessRuleException("El empleado ya calificó este bootcamp");
        }

        CalificacionBootcampEntity calificacion = new CalificacionBootcampEntity();
        calificacion.setBootcamp(bootcamp);
        calificacion.setEmployee(employee);
        calificacion.setPuntaje(dto.puntaje());
        calificacion.setComentario(dto.comentario());
        calificacion.setFechaComentario(LocalDate.now());

        calificacion = calificacionBootcampRepository.save(calificacion);
        return mapper.toDto(calificacion);
    }

    public void validarCalificacion(CalificacionBootcampRequestDTO dto) {

        if (dto.employeeId() == null) {
            throw new BadRequestException("El empleado es obligatorio");
        }

        if (dto.puntaje() == null) {
            throw new BadRequestException("El puntaje es obligatorio");
        }

        if (dto.puntaje() < 1 || dto.puntaje() > 5) {
            throw new BadRequestException("El puntaje debe estar entre 1 y 5");
        }

        if (dto.comentario() == null || dto.comentario().isBlank()) {
            throw new BadRequestException("El comentario es obligatorio");
        }

        if (dto.comentario().length() > 255) {
            throw new BadRequestException("El comentario no puede superar los 255 caracteres");
        }
    }
}
