package com.example.Proyecto_Final_Progra3.service.bootcamp.inscripcion;

import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEmpresa;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampParticular;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.bootcamps.inscripcion.*;
import com.example.Proyecto_Final_Progra3.model.enums.EstadoInscripcion;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampParticularRepository;
import com.example.Proyecto_Final_Progra3.repository.employee.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.inscripcion.InscripcionBootcampParticularRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Service
public class InscripcionBootcampParticularService {
    private final InscripcionBootcampParticularRepository inscripcionBootcampParticularRepository;
    private final BootcampParticularRepository bootcampParticularRepository;
    private final EmployeeRepository employeeRepository;
    public InscripcionBootcampParticular inscribirEmpleadoABootcampParticular(Integer bootcampId, Integer employeeId) {

        BootcampParticular bootcamp = bootcampParticularRepository.findById(bootcampId)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp particular con ese ID"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("No existe un empleado con ese ID"));

        validarInscripcionBootcampParticular(bootcamp, employee);

        InscripcionBootcampParticular inscripcion = new InscripcionBootcampParticular();
        inscripcion.setBootcampParticular(bootcamp);
        inscripcion.setEmployee(employee);
        inscripcion.setFechaInscripcion(LocalDate.now());
        inscripcion.setEstado(EstadoInscripcion.INSCRIPTO);

        bootcamp.setCupos(bootcamp.getCupos() - 1);
        bootcampParticularRepository.save(bootcamp);

        return inscripcionBootcampParticularRepository.save(inscripcion);
    }
    private void validarInscripcionBootcampParticular(BootcampParticular bootcamp, Employee employee) {

        if (bootcamp.getCupos() == null || bootcamp.getCupos() <= 0) {
            throw new RuntimeException("No hay cupos disponibles para este bootcamp");
        }

        if (bootcamp.getFechaInicio().isBefore(LocalDate.now()) || bootcamp.getFechaInicio().isEqual(LocalDate.now())) {
            throw new RuntimeException("No se puede inscribir a un bootcamp que ya comenzó o comienza hoy");
        }

        boolean yaInscripto = inscripcionBootcampParticularRepository
                .existsByBootcampParticularAndEmployee(bootcamp, employee);

        if (yaInscripto) {
            throw new RuntimeException("El empleado ya está inscripto en este bootcamp");
        }
    }
}
