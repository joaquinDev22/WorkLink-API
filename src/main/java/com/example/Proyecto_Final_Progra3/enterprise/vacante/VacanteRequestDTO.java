package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record VacanteRequestDTO(
        @NotBlank(message = "El puesto es obligatorio")
        String puesto,
        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,
        @NotNull(message = "La fecha de finalización es obligatoria")
        @Future(message = "La fecha de finalización debe ser futura")
        LocalDate finalizacion,
        Integer empresaId,
        String logo,
        String ubicacion,
        String tipoUbicacion,
        String tipoVacante,
        String salario,
        List<String> etiquetas
) {}
