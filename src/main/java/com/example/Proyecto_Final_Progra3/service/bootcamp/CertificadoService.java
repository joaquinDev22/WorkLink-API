package com.example.Proyecto_Final_Progra3.service.bootcamp;

import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.CertificadoBootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Inscripcion;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.EstadoInscripcion;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.CertificadoBootcampRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InscripcionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class CertificadoService {
    private final CertificadoBootcampRepository certificadoBootcampRepository;
    private final InscripcionRepository inscripcionRepository;

    @Transactional
    public CertificadoBootcamp generarCertificado(Integer inscripcionId) {

        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new RuntimeException("No existe una inscripción con ese ID"));

        if (inscripcion.getEstado() == EstadoInscripcion.CANCELADO) {
            throw new RuntimeException("No se puede generar certificado de una inscripción cancelada");
        }

        if (inscripcion.getEstado() == EstadoInscripcion.COMPLETADO) {
            throw new RuntimeException("Esta inscripción ya fue completada");
        }

        Bootcamp bootcamp = inscripcion.getBootcamp();
        Employee employee = inscripcion.getEmployee();

        boolean yaTieneCertificado = certificadoBootcampRepository.existsByBootcampAndEmployee(bootcamp, employee);

        if (yaTieneCertificado) {
            throw new RuntimeException("El empleado ya tiene un certificado para este bootcamp");
        }

        inscripcion.setEstado(EstadoInscripcion.COMPLETADO);
        inscripcionRepository.save(inscripcion);

        CertificadoBootcamp certificado = new CertificadoBootcamp();
        certificado.setBootcamp(bootcamp);
        certificado.setEmployee(employee);
        certificado.setFechaEmision(LocalDate.now());
        certificado.setCodigoCertificado(generarCodigoCertificado(bootcamp, employee));

        return certificadoBootcampRepository.save(certificado);
    }

    private String generarCodigoCertificado(Bootcamp bootcamp, Employee employee) {
        return "BOOT-" + bootcamp.getId() + "-EMP-" + employee.getEmployeeId();
    }
}

