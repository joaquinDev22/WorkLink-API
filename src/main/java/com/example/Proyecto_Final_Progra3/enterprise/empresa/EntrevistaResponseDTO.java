package com.example.Proyecto_Final_Progra3.enterprise.empresa;

public record EntrevistaResponseDTO(
    Integer id,
    String candidateName,
    String vacancyTitle,
    String date,
    String time,
    String link
) {
}
