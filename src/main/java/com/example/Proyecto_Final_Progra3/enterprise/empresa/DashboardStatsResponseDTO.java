package com.example.Proyecto_Final_Progra3.enterprise.empresa;

public record DashboardStatsResponseDTO(
        Long totalVacantes,
        Long totalPostulaciones,
        Long entrevistasPendientes,
        Long contratacionesEfectuadas
) {
}
