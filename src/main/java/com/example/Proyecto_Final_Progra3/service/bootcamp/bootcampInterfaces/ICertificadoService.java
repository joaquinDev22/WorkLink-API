package com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampInterfaces;

import com.example.Proyecto_Final_Progra3.dto.responsedto.CertificadoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;

public interface ICertificadoService {
    CertificadoResponseDTO generarCertificado(Integer inscripcionId);
    String generarCodigoCertificado(BootcampEntity bootcamp, EmployeeEntity employee);
}

