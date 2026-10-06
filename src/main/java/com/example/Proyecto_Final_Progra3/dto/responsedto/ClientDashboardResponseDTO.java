package com.example.Proyecto_Final_Progra3.dto.responsedto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Métricas estadísticas del dashboard del cliente autenticado basadas en datos reales de base de datos")
public record ClientDashboardResponseDTO(
        @Schema(description = "Cantidad de proyectos actualmente activos o en búsqueda de talento", example = "2")
        long activeProjects,

        @Schema(description = "Cantidad de proyectos completados/finalizados con éxito", example = "5")
        long completedProjects,

        @Schema(description = "Monto total acumulado invertido en pagos de proyectos", example = "4500.0")
        Double totalSpent,

        @Schema(description = "Cantidad total de propuestas recibidas para sus proyectos", example = "3")
        long pendingProposals
) {}

