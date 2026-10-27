package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CalificacionProyectoRequestDTO(
        @NotNull(message = "El puntaje es obligatorio")
        @Min(value = 1, message = "El puntaje mínimo es 1")
        @Max(value = 5, message = "El puntaje máximo es 5")
        Integer puntaje,

        @NotBlank(message = "El comentario es obligatorio")
        @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
        String comentario
) {}

