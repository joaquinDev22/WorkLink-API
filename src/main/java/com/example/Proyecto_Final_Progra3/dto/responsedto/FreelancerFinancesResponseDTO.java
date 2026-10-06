package com.example.Proyecto_Final_Progra3.dto.responsedto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumen financiero del freelancer autenticado basado en datos reales de base de datos")
public record FreelancerFinancesResponseDTO(
        @Schema(description = "Saldo disponible total cobrado por proyectos finalizados", example = "1250.0")
        Double balance,

        @Schema(description = "Monto pendiente correspondiente a proyectos en curso", example = "450.0")
        Double pending,

        @Schema(description = "Monto total acumulado histórico", example = "3000.0")
        Double withdrawn
) {}

