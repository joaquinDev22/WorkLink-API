package com.example.Proyecto_Final_Progra3.service.bootcamp;


import com.example.Proyecto_Final_Progra3.model.Employee;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Inscripcion;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.EstadoInscripcion;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InscripcionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InscripcionService {
    private final InscripcionRepository repository;
    private final BootcampRepository bootcampRepository;
    private final EmployeeRepository employeeRepository;

    public InscripcionService(InscripcionRepository repository,  BootcampRepository bootcampRepository, EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.bootcampRepository = bootcampRepository;
        this.employeeRepository = employeeRepository;
    }

    public Inscripcion realizarInscripcion(Integer bootcampId, Integer employeeId) {

        Bootcamp bootcamp = bootcampRepository.findById(bootcampId)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp particular con ese ID"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("No existe un empleado con ese ID"));

        validarInscripcionBootcampParticular(bootcamp, employee);

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setBootcamp(bootcamp);
        inscripcion.setEmployee(employee);
        inscripcion.setInscripcion(LocalDate.now());
        inscripcion.setEstado(EstadoInscripcion.INSCRIPTO);

        bootcamp.setCupos(bootcamp.getCupos() - 1);
        bootcampRepository.save(bootcamp);

        return repository.save(inscripcion);
    }

    private void validarInscripcionBootcampParticular(Bootcamp bootcamp, Employee employee) {

        if (bootcamp.getCupos() <= 0) {
            throw new RuntimeException("No hay cupos disponibles para este bootcamp");
        }

        if (bootcamp.getInicio().isBefore(LocalDate.now()) || bootcamp.getInicio().isEqual(LocalDate.now())) {
            throw new RuntimeException("No se puede inscribir a un bootcamp que ya comenzó o comienza hoy");
        }

        boolean yaInscripto = repository.existsByBootcampAndEmployee(bootcamp, employee);

        if (yaInscripto) {
            throw new RuntimeException("El empleado ya está inscripto en este bootcamp");
        }
    }
}
