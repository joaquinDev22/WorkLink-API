package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IPagosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pagos/webhook")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class MercadoPagoWebhookController {

    private final IPagosService pagosService;

    @Operation(summary = "Webhook de Mercado Pago", description = "Receptor público de notificaciones asíncronas de eventos de pago (IPN/Webhooks) de Mercado Pago.")
    @PostMapping
    public ResponseEntity<Void> recibirNotificacion(
            @RequestParam(name = "topic", required = false) String topic,
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "data.id", required = false) String dataId) {

        log.info("Notificación Webhook recibida de Mercado Pago: topic={}, id={}, type={}, data.id={}", topic, id, type, dataId);
        pagosService.procesarWebhook(topic, id, type, dataId);
        return ResponseEntity.ok().build();
    }
}

