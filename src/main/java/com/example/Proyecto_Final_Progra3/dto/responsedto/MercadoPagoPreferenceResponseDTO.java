package com.example.Proyecto_Final_Progra3.dto.responsedto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con los datos de la preferencia de pago generada en Mercado Pago Checkout Pro")
public record MercadoPagoPreferenceResponseDTO(
        @Schema(description = "Identificador único de la preferencia en Mercado Pago", example = "123456789-abcd-1234")
        String preferenceId,

        @Schema(description = "URL de Checkout Pro para redirigir al usuario al entorno de pago de Mercado Pago", example = "https://www.mercadopago.com.ar/checkout/v1/redirect?pref_id=...")
        String initPoint,

        @Schema(description = "URL de Checkout Pro para pruebas en entorno Sandbox", example = "https://sandbox.mercadopago.com.ar/checkout/v1/redirect?pref_id=...")
        String sandboxInitPoint
) {}

