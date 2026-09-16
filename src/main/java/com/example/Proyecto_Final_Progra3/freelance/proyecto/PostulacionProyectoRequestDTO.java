package com.example.Proyecto_Final_Progra3.freelance.proyecto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostulacionProyectoRequestDTO(
        @NotBlank(message = "La descripción/propuesta es obligatoria") String descripcion,
        @NotNull(message = "El costo propuesto es obligatorio") Double costo,
        @NotNull(message = "El ID del freelancer (employee) es obligatorio") Integer employeeId
) {}
