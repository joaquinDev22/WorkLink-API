package com.example.Proyecto_Final_Progra3.dto.responsedto;

import java.time.LocalDate;

public record CalificacionProyectoResponseDTO(
        Integer calificacionId,
        Integer puntaje,
        String comentario,
        LocalDate fechaComentario,
        Integer proyectoId
) {}

