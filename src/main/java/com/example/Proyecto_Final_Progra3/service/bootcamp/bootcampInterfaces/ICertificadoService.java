package com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampInterfaces;

import com.example.Proyecto_Final_Progra3.dto.responsedto.CertificadoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;

public interface ICertificadoService {
    CertificadoResponseDTO generarCertificado(Integer inscripcionId);
    String generarCodigoCertificado(Bootcamp bootcamp, Employee employee);
}
