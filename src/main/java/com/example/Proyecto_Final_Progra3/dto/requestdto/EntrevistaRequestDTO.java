package com.example.Proyecto_Final_Progra3.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Datos para agendar una entrevista vinculada a una postulación de vacante")
public record EntrevistaRequestDTO(
        @NotNull(message = "El identificador de la postulación no puede ser nulo")
        @Schema(description = "Identificador de la postulación asociada", example = "1")
        Integer postulacionId,

        @NotNull(message = "La fecha no puede ser nula")
        @Future(message = "La entrevista debe agendarse a partir del día siguiente")
        @Schema(description = "Fecha de la entrevista", example = "2026-10-15")
        LocalDate fecha,

        @NotNull(message = "La hora no puede ser nula")
        @Schema(description = "Hora de la entrevista", example = "14:30:00")
        LocalTime hora,

        @Schema(description = "Enlace de videoconferencia para la reunión", example = "https://meet.google.com/abc-defg-hij")
        String link
) {}

