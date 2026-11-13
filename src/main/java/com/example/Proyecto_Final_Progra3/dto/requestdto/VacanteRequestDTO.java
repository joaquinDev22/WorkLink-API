package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record VacanteRequestDTO(
        @NotBlank(message = "El puesto es obligatorio")
        @Size(max = 50, message = "El puesto no puede superar los 50 caracteres") String puesto,
        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 255, message = "La descripción no puede superar los 255 caracteres") String descripcion,
        @NotNull(message = "La fecha de finalización es obligatoria")
        @Future(message = "La fecha de finalización debe ser futura")
        LocalDate finalizacion,
        Integer empresaId,
        @Size(max = 255, message = "El logo no puede superar los 255 caracteres") String logo,
        @Size(max = 100, message = "La ubicación no puede superar los 100 caracteres") String ubicacion,
        @Size(max = 50, message = "El tipo de ubicación no puede superar los 50 caracteres") String tipoUbicacion,
        @Size(max = 50, message = "El tipo de vacante no puede superar los 50 caracteres") String tipoVacante,
        @Size(max = 50, message = "El salario no puede superar los 50 caracteres") String salario,
        List<String> etiquetas
) {}
