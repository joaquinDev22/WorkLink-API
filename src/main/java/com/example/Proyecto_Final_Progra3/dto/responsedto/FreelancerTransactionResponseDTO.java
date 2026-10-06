package com.example.Proyecto_Final_Progra3.dto.responsedto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalle de una transacción o pago recibido por el freelancer persistido en base de datos")
public record FreelancerTransactionResponseDTO(
        @Schema(description = "Identificador del pago", example = "1")
        Integer id,

        @Schema(description = "Moneda del pago", example = "PESOS")
        String currency,

        @Schema(description = "Monto de la transacción", example = "500")
        Integer amount,

        @Schema(description = "Medio de pago utilizado", example = "BILLETERA_VIRTUAL")
        String paymentMethod,

        @Schema(description = "Identificador del proyecto asociado", example = "2")
        Integer projectId,

        @Schema(description = "Área o temática del proyecto", example = "Desarrollo Backend Java")
        String projectTitle
) {}

