package com.example.Proyecto_Final_Progra3.dto.responsedto;

public record DashboardStatsResponseDTO(
        Long totalVacantes,
        Long totalPostulaciones,
        Long entrevistasPendientes,
        Long contratacionesEfectuadas
) {
}
