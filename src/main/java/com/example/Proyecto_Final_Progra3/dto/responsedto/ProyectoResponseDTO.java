package com.example.Proyecto_Final_Progra3.dto.responsedto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProyectoResponseDTO(
        Integer proyectoId,
        String area,
        String descripcion,
        LocalDate fechaPublicacion,
        BigDecimal presupuestoMax,
        BigDecimal presupuestoMin,
        Integer clienteId,
        String nombreCliente,
        String duracion,
        String tipoPago,
        List<String> habilidades,
        String estado,
        String nombreFreelancer
) {}
