package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import java.time.LocalDate;

public record CertificadoResponseDTO(
        Integer id,
        String codigoCertificado,
        LocalDate fechaEmision,
        Integer bootcamp_id,
        Integer employee_id
) {}
