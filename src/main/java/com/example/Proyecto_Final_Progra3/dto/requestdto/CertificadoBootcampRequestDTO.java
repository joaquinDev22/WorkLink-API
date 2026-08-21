package com.example.Proyecto_Final_Progra3.dto.requestdto;

import java.time.LocalDate;

public record CertificadoBootcampRequestDTO(
        Integer certificadoId,
        String codigoCertificado,
        LocalDate fechaEmision,
        String nombreEmpleado,
        String tituloBootcamp,
        String mensaje
) {}
