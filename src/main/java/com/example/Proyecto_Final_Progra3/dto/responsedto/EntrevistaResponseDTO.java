package com.example.Proyecto_Final_Progra3.dto.responsedto;

public record EntrevistaResponseDTO(
    Integer id,
    Integer postulacionId,
    String candidateName,
    String candidateEmail,
    String vacancyTitle,
    String companyName,
    String date,
    String time,
    String link,
    String status          // PROGRAMADA si todavía no pasó la fecha/hora, COMPLETADA si ya pasó
) {
}
