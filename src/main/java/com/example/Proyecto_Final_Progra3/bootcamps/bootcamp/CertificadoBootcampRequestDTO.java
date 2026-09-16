package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import java.time.LocalDate;

public record CertificadoBootcampRequestDTO(
        Integer certificadoId,
        String codigoCertificado,
        LocalDate fechaEmision,
        String nombreEmpleado,
        String tituloBootcamp,
        String mensaje
) {}
