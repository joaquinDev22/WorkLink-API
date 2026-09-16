package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import jakarta.validation.constraints.*;

public record CalificacionBootcampRequestDTO(
        @NotNull(message = "El ID del empleado es obligatorio") Integer employeeId,
        @NotNull(message = "El puntaje es obligatorio")
        @Min(value = 1, message = "El puntaje mínimo es 1")
        @Max(value = 5, message = "El puntaje máximo es 5")
        Integer puntaje,

        @NotBlank(message = "El comentario es obligatorio")
        @Size(max = 255, message = "El comentario no puede superar los 255 caracteres")
        String comentario
) {}

