package com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampImpl;

import com.example.Proyecto_Final_Progra3.dto.responsedto.CertificadoResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.mappers.CertificadoMapper;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.CertificadoBootcampEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.InscripcionEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.EstadoInscripcion;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.CertificadoBootcampRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InscripcionRepository;
import com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampInterfaces.ICertificadoService;
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
