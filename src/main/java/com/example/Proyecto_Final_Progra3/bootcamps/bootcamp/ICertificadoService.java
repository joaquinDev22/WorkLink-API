package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;

public interface ICertificadoService {
    CertificadoResponseDTO generarCertificado(Integer inscripcionId);
    String generarCodigoCertificado(BootcampEntity bootcamp, EmployeeEntity employee);
}

