package com.example.Proyecto_Final_Progra3.service.bootcamp.inscripcion;

import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEmpresa;
import com.example.Proyecto_Final_Progra3.model.bootcamps.inscripcion.InscripcionBootcampEmpresa;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.enums.EstadoInscripcion;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampEmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.employee.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.inscripcion.InscripcionBootcampEmpresaRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Service

public class InscripcionBootcampEmpresaService {
    private final InscripcionBootcampEmpresaRepository inscripcionBootcampEmpresaRepository;
    private final BootcampEmpresaRepository bootcampEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final EmployeeRepository employeeRepository;


    public InscripcionBootcampEmpresa inscribirEmpleadoABootcampEmpresa(Integer bootcampId, Integer employeeId) {

        BootcampEmpresa bootcamp = bootcampEmpresaRepository.findById(bootcampId)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp empresarial con ese ID"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("No existe un empleado con ese ID"));

        validarInscripcionBootcampEmpresa(bootcamp, employee);

        InscripcionBootcampEmpresa inscripcion = new InscripcionBootcampEmpresa();
        inscripcion.setBootcampEmpresa(bootcamp);
        inscripcion.setEmployee(employee);
        inscripcion.setFechaInscripcion(LocalDate.now());
        inscripcion.setEstado(EstadoInscripcion.INSCRIPTO);

        bootcamp.setCupos(bootcamp.getCupos() - 1);
        bootcampEmpresaRepository.save(bootcamp);

        return inscripcionBootcampEmpresaRepository.save(inscripcion);
    }
    private void validarInscripcionBootcampEmpresa(BootcampEmpresa bootcamp, Employee employee) {

        if (bootcamp.getCupos() == null || bootcamp.getCupos() <= 0) {
            throw new RuntimeException("No hay cupos disponibles para este bootcamp");
        }

        if (bootcamp.getFechaInicio().isBefore(LocalDate.now()) ||
        bootcamp.getFechaInicio().isEqual(LocalDate.now())) {
            throw new RuntimeException("No se puede inscribir a un bootcamp que ya comenzó o comienza hoy");
        }

        boolean yaInscripto = inscripcionBootcampEmpresaRepository
                .existsByBootcampEmpresaAndEmployee(bootcamp, employee);

        if (yaInscripto) {
            throw new RuntimeException("El empleado ya está inscripto en este bootcamp");
        }
    }
}
