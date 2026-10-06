package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.responsedto.MercadoPagoPreferenceResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Adaptador que encapsula todas las llamadas externas al SDK de Mercado Pago.
 * REGLA ARQUITECTÓNICA: Esta clase NO lleva anotación @Transactional para evitar agotar el pool de conexiones HikariCP.
 */
@Component
@Slf4j
public class MercadoPagoAdapter {

    @Value("${mercadopago.webhook-url:https://worklink.com/api/v1/pagos/webhook}")
    private String webhookUrl;

    public MercadoPagoPreferenceResponseDTO crearPreferencia(Integer proyectoId, String titulo, BigDecimal monto, String backUrlBase) {
        try {
            PreferenceClient client = new PreferenceClient();

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title(titulo != null && !titulo.isBlank() ? titulo : "Pago de Proyecto WorkLink #" + proyectoId)
                    .quantity(1)
                    .unitPrice(monto)
                    .currencyId("ARS")
                    .build();

            String successUrl = (backUrlBase != null ? backUrlBase : "https://worklink.com") + "/pagos/success";
            String failureUrl = (backUrlBase != null ? backUrlBase : "https://worklink.com") + "/pagos/failure";
            String pendingUrl = (backUrlBase != null ? backUrlBase : "https://worklink.com") + "/pagos/pending";

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(successUrl)
                    .failure(failureUrl)
                    .pending(pendingUrl)
                    .build();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(List.of(item))
                    .backUrls(backUrls)
                    .notificationUrl(webhookUrl)
                    .externalReference(String.valueOf(proyectoId))
                    .autoReturn("approved")
                    .build();

            Preference preference = client.create(request);
            log.info("Preferencia de Mercado Pago creada con éxito: id={}", preference.getId());

            return new MercadoPagoPreferenceResponseDTO(
                    preference.getId(),
                    preference.getInitPoint(),
                    preference.getSandboxInitPoint()
            );
        } catch (MPApiException apiException) {
            log.error("Error en API de Mercado Pago: status={}, response={}", 
                    apiException.getStatusCode(), apiException.getApiResponse().getContent());
            throw new BusinessRuleException("Error devuelto por la pasarela de pagos Mercado Pago: " + apiException.getMessage());
        } catch (MPException mpException) {
            log.error("Excepción en SDK de Mercado Pago: {}", mpException.getMessage());
            throw new BusinessRuleException("Fallo en la comunicación con la pasarela de pagos: " + mpException.getMessage());
        }
    }

    public Payment consultarPago(Long paymentId) {
        try {
            PaymentClient client = new PaymentClient();
            return client.get(paymentId);
        } catch (MPApiException apiException) {
            log.error("Error al consultar pago en Mercado Pago: status={}, response={}", 
                    apiException.getStatusCode(), apiException.getApiResponse().getContent());
            throw new BusinessRuleException("No se pudo verificar el pago en Mercado Pago: " + apiException.getMessage());
        } catch (MPException mpException) {
            log.error("Excepción al consultar pago en Mercado Pago: {}", mpException.getMessage());
            throw new BusinessRuleException("Fallo al verificar el estado del pago con Mercado Pago: " + mpException.getMessage());
        }
    }
}

