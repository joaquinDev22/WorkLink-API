package com.example.Proyecto_Final_Progra3.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Observador de eventos de dominio (GoF Observer Pattern).
 * Escucha los eventos publicados por el ApplicationEventPublisher de Spring,
 * procesando registros de auditoría y notificaciones sin acoplar los servicios de negocio.
 */
@Slf4j
@Component
public class AuditEventListener {

    @EventListener
    public void onPagoConfirmado(PagoConfirmadoEvent event) {
        log.info("[AUDITORÍA / EVENTO OBSERVER] Pago registrado/confirmado -> ID Pago: {}, Proyecto: {}, Monto: {} {}, Medio: {}, Fecha: {}",
                event.pagoId(), event.proyectoId(), event.monto(), event.moneda(), event.medioDePago(), event.timestamp());
    }

    @EventListener
    public void onEntrevistaAgendada(EntrevistaAgendadaEvent event) {
        log.info("[AUDITORÍA / EVENTO OBSERVER] Nueva entrevista agendada -> ID Entrevista: {}, Postulación: {}, Recruiter: {}, Postulante: {}, Fecha: {} {}, Link: {}",
                event.entrevistaId(), event.postulacionId(), event.recruiterUsername(), event.postulanteEmail(), event.fecha(), event.hora(), event.link());
    }
}

