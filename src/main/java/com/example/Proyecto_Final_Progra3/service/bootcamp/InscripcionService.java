package com.example.Proyecto_Final_Progra3.service.bootcamp;


import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Inscripcion;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.EstadoInscripcion;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InscripcionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class InscripcionService {
    private final InscripcionRepository inscripcionRepository;
    private final BootcampRepository bootcampRepository;
    private final EmployeeRepository employeeRepository;

    public Inscripcion realizarInscripcion(Integer bootcampId, Integer employeeId) {

        Bootcamp bootcamp = bootcampRepository.findById(bootcampId)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp particular con ese ID"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("No existe un empleado con ese ID"));

        validarInscripcionBootcamp(bootcamp, employee);

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setBootcamp(bootcamp);
        inscripcion.setEmployee(employee);
        inscripcion.setInscripcion(LocalDate.now());
        inscripcion.setEstado(EstadoInscripcion.INSCRIPTO);

        bootcamp.setCupos(bootcamp.getCupos() - 1);
        bootcampRepository.save(bootcamp);

        return inscripcionRepository.save(inscripcion);
    }

    public Inscripcion cancelarInscripcion(Integer inscripcionId) {

        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new RuntimeException("No existe una inscripción con ese ID"));

        if (inscripcion.getEstado() == EstadoInscripcion.CANCELADO) {
            throw new RuntimeException("La inscripción ya se encuentra cancelada");
        }

        Bootcamp bootcamp = inscripcion.getBootcamp();

        inscripcion.setEstado(EstadoInscripcion.CANCELADO);

        bootcamp.setCupos(bootcamp.getCupos() + 1);

        bootcampRepository.save(bootcamp);

        return inscripcionRepository.save(inscripcion);
    }


    private void validarInscripcionBootcamp(Bootcamp bootcamp, Employee employee) {

        if (bootcamp.getCupos() <= 0) {
            throw new RuntimeException("No hay cupos disponibles para este bootcamp");
        }

        if (bootcamp.getInicio().isBefore(LocalDate.now()) || bootcamp.getInicio().isEqual(LocalDate.now())) {
            throw new RuntimeException("No se puede inscribir a un bootcamp que ya comenzó o comienza hoy");
        }

        boolean yaInscripto = inscripcionRepository.existsByBootcampAndEmployee(bootcamp, employee);

        if (yaInscripto) {
            throw new RuntimeException("El empleado ya está inscripto en este bootcamp");
        }
    }
}
