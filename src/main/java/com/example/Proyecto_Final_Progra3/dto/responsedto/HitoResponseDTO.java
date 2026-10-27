package com.example.Proyecto_Final_Progra3.dto.responsedto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HitoResponseDTO(
        Integer hitoId,
        String titulo,
        String descripcion,
        BigDecimal monto,
        String estado,
        LocalDate fechaLimite,
        Integer proyectoId
) {}

