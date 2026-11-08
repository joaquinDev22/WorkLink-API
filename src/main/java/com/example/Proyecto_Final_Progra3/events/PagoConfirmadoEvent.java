package com.example.Proyecto_Final_Progra3.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento de dominio emitido cuando un pago es registrado o confirmado en la plataforma.
 * Implementa el patrón GoF Observer a través del bus de eventos de Spring.
 */
public record PagoConfirmadoEvent(
        Integer pagoId,
        Integer proyectoId,
        BigDecimal monto,
        String moneda,
        String medioDePago,
        LocalDateTime timestamp
) {
    public PagoConfirmadoEvent(Integer pagoId, Integer proyectoId, BigDecimal monto, String moneda, String medioDePago) {
        this(pagoId, proyectoId, monto, moneda, medioDePago, LocalDateTime.now());
    }
}

