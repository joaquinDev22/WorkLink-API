package com.example.Proyecto_Final_Progra3.service.freelance.strategy;

import com.example.Proyecto_Final_Progra3.exceptions.domain.BadRequestException;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Fábrica y Registro de Estrategias de Pago (GoF Strategy + Factory).
 * Inyecta dinámicamente todas las implementaciones de IPagoStrategy registradas
 * en el contexto de Spring y despacha la adecuada sin necesidad de acoplamiento rígido (OCP).
 */
@Component
@RequiredArgsConstructor
public class PagoStrategyFactory {

    private final List<IPagoStrategy> strategies;

    /**
     * Resuelve la estrategia adecuada para el medio de pago recibido.
     * @param medioDePago Medio de pago a resolver
     * @return Estrategia que da soporte a dicho medio
     */
    public IPagoStrategy getStrategy(MedioDePago medioDePago) {
        if (medioDePago == null) {
            throw new BadRequestException("El medio de pago no puede ser nulo.");
        }
        return strategies.stream()
                .filter(strategy -> strategy.supports(medioDePago))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Medio de pago no soportado en la plataforma: " + medioDePago));
    }
}

