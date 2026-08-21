package com.example.Proyecto_Final_Progra3.dto.responsedto;

import java.time.LocalDate;

public record CalificacionBootcampResponseDTO(
        Integer id,
        Integer puntaje,
        String comentario,
        LocalDate fechaComentario,
        Integer bootcamp_id,
        Integer employee_id
) {}
