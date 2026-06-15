package com.example.Proyecto_Final_Progra3.dto.responsedto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CalificacionBootcampResponseDTO {
    private Integer id;
    private Integer puntaje;
    private String comentario;
    private LocalDate fechaComentario;
    private Integer bootcamp_id;
    private Integer employee_id;
}
