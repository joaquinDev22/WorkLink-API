package com.example.Proyecto_Final_Progra3.model.enterprise;

/**
 * Etapas de una postulación, en el orden en que avanza el proceso.
 * El orden de declaración importa: una postulación solo puede avanzar, nunca retroceder.
 */
public enum EstadoPostulacion {
    PENDIENTE,
    VISUALIZADO,
    ENTREVISTA_AGENDADA,
    RECHAZADO;

    /** Information Expert: el propio estado sabe a cuáles puede pasar. */
    public boolean puedeAvanzarA(EstadoPostulacion nuevo) {
        return nuevo != null && nuevo.ordinal() > this.ordinal();
    }

    public boolean esFinal() {
        return this == RECHAZADO;
    }
}
