package com.example.Proyecto_Final_Progra3.events;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Evento de dominio emitido cuando se agenda una nueva entrevista laboral.
 * Implementa el patrón GoF Observer a través del bus de eventos de Spring.
 */
public record EntrevistaAgendadaEvent(
        Integer entrevistaId,
        Integer postulacionId,
        String recruiterUsername,
        String postulanteEmail,
        LocalDate fecha,
        LocalTime hora,
        String link,
        LocalDateTime timestamp
) {
    public EntrevistaAgendadaEvent(Integer entrevistaId, Integer postulacionId, String recruiterUsername, String postulanteEmail, LocalDate fecha, LocalTime hora, String link) {
        this(entrevistaId, postulacionId, recruiterUsername, postulanteEmail, fecha, hora, link, LocalDateTime.now());
    }
}

