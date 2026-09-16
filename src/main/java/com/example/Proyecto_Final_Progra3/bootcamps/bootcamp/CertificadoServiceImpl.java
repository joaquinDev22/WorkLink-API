package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import com.example.Proyecto_Final_Progra3.errors.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class CertificadoServiceImpl implements ICertificadoService {
    private final CertificadoBootcampRepository certificadoBootcampRepository;
    private final InscripcionRepository inscripcionRepository;
    private final CertificadoMapper mapper;

    @Transactional
    public CertificadoResponseDTO generarCertificado(Integer inscripcionId) {

        InscripcionEntity inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una inscripción con el ID: " + inscripcionId));

        if (inscripcion.getEstado() == EstadoInscripcion.CANCELADO) {
            throw new BusinessRuleException("No se puede generar certificado de una inscripción cancelada");
        }

        if (inscripcion.getEstado() == EstadoInscripcion.COMPLETADO) {
            throw new BusinessRuleException("Esta inscripción ya fue completada");
        }

        BootcampEntity bootcamp = inscripcion.getBootcamp();
        EmployeeEntity employee = inscripcion.getEmployee();

        boolean yaTieneCertificado = certificadoBootcampRepository.existsByBootcampAndEmployee(bootcamp, employee);

        if (yaTieneCertificado) {
            throw new BusinessRuleException("El empleado ya tiene un certificado emitido para este bootcamp");
        }

        inscripcion.setEstado(EstadoInscripcion.COMPLETADO);
        inscripcionRepository.save(inscripcion);

        CertificadoBootcampEntity certificado = new CertificadoBootcampEntity();
        certificado.setBootcamp(bootcamp);
        certificado.setEmployee(employee);
        certificado.setFechaEmision(LocalDate.now());
        certificado.setCodigoCertificado(generarCodigoCertificado(bootcamp, employee));

        certificado = certificadoBootcampRepository.save(certificado);
        return mapper.toDto(certificado);
    }

    @Override
    public String generarCodigoCertificado(BootcampEntity bootcamp, EmployeeEntity employee) {
        return "BOOT-" + bootcamp.getId() + "-EMP-" + employee.getEmployeeId();
    }
}
