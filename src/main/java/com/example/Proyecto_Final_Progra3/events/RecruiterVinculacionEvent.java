package com.example.Proyecto_Final_Progra3.events;

import java.time.LocalDateTime;

/**
 * Evento de dominio emitido cuando cambia la vinculación entre un recruiter y una empresa.
 * Implementa el patrón GoF Observer a través del bus de eventos de Spring.
 */
public record RecruiterVinculacionEvent(
        Tipo tipo,
        Integer recruiterId,
        String recruiterEmail,
        String empresa,
        LocalDateTime timestamp
) {
    public enum Tipo {
        VINCULADO,
        BAJA_SOLICITADA,
        BAJA_ACEPTADA,
        BAJA_POR_CUENTA_PROPIA,
        DESVINCULADO_POR_EMPRESA
    }

    public RecruiterVinculacionEvent(Tipo tipo, Integer recruiterId, String recruiterEmail, String empresa) {
        this(tipo, recruiterId, recruiterEmail, empresa, LocalDateTime.now());
    }
}
