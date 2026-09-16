package com.example.Proyecto_Final_Progra3.freelance.proyecto;

import java.time.LocalDate;

public record PostulacionProyectoResponseDTO(
        Integer postulacionId,
        String descripcion,
        Double costoPropuesto,
        LocalDate fechaPostulacion,
        Integer empleadoId,
        String nombreEmpleado,
        Integer proyectoId
) {}
