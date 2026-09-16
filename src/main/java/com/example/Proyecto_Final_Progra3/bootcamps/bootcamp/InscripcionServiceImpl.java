package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import com.example.Proyecto_Final_Progra3.errors.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.errors.UnauthorizedAccessException;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class InscripcionServiceImpl implements IInscripcionService {
    private final InscripcionRepository inscripcionRepository;
    private final BootcampRepository bootcampRepository;
    private final EmployeeRepository employeeRepository;
    private final PerfilRepository perfilRepository;
    private final InscripcionMapper mapper;

    @Transactional
    public InscripcionResponseDTO realizarInscripcion(Integer bootcampId, Integer employeeId) {

        BootcampEntity bootcamp = bootcampRepository.findById(bootcampId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un bootcamp con el ID: " + bootcampId));

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un empleado con el ID: " + employeeId));

        validarInscripcionBootcamp(bootcamp, employee);

        InscripcionEntity inscripcion = new InscripcionEntity();
        inscripcion.setBootcamp(bootcamp);
        inscripcion.setEmployee(employee);
        inscripcion.setInscripcion(LocalDate.now());
        inscripcion.setEstado(EstadoInscripcion.INSCRIPTO);

        bootcamp.setCupos(bootcamp.getCupos() - 1);
        bootcampRepository.save(bootcamp);

        inscripcion = inscripcionRepository.save(inscripcion);
        return mapper.toDto(inscripcion);
    }

    @Transactional
    public InscripcionResponseDTO realizarMiInscripcion(String username, Integer bootcampId) {
        PerfilEntity perfil = perfilRepository
                .findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        EmployeeEntity employee = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("Solo los empleados o freelancers pueden inscribirse en bootcamps"));
                
        return realizarInscripcion(bootcampId, employee.getEmployeeId());
    }

    public List<InscripcionResponseDTO> obtenerMisInscripciones(String username) {
        PerfilEntity perfil = perfilRepository
                .findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        EmployeeEntity employee = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("Solo los empleados o freelancers pueden consultar sus inscripciones"));
                
        return mapper.toDtoList(inscripcionRepository.findByEmployee(employee));
    }

    @Transactional
    public InscripcionResponseDTO cancelarInscripcion(Integer inscripcionId) {

        InscripcionEntity inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una inscripción con el ID: " + inscripcionId));

        if (inscripcion.getEstado() == EstadoInscripcion.CANCELADO) {
            throw new BusinessRuleException("La inscripción ya se encuentra cancelada");
        }

        BootcampEntity bootcamp = inscripcion.getBootcamp();

        inscripcion.setEstado(EstadoInscripcion.CANCELADO);

        bootcamp.setCupos(bootcamp.getCupos() + 1);

        bootcampRepository.save(bootcamp);

        inscripcion = inscripcionRepository.save(inscripcion);
        return mapper.toDto(inscripcion);
    }

    public void validarInscripcionBootcamp(BootcampEntity bootcamp, EmployeeEntity employee) {

        if (bootcamp.getCupos() <= 0) {
            throw new BusinessRuleException("No hay cupos disponibles para este bootcamp");
        }

        if (bootcamp.getInicio().isBefore(LocalDate.now()) || bootcamp.getInicio().isEqual(LocalDate.now())) {
            throw new BusinessRuleException("No se puede inscribir a un bootcamp que ya comenzó o comienza hoy");
        }

        boolean yaInscripto = inscripcionRepository.existsByBootcampAndEmployee(bootcamp, employee);

        if (yaInscripto) {
            throw new BusinessRuleException("El empleado ya está inscripto en este bootcamp");
        }
    }
}
