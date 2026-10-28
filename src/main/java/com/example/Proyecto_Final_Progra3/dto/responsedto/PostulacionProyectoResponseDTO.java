package com.example.Proyecto_Final_Progra3.dto.responsedto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PostulacionProyectoResponseDTO(
        Integer postulacionId,
        String descripcion,
        BigDecimal costoPropuesto,
        LocalDate fechaPostulacion,
        String estado,
        Integer empleadoId,
        String nombreEmpleado,
        Integer proyectoId
) {}
