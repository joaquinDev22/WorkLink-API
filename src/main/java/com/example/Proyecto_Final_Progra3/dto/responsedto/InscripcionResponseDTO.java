package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.EstadoInscripcion;
import lombok.Data;
import java.time.LocalDate;

@Data
public class InscripcionResponseDTO {
    private Integer id;
    private LocalDate inscripcion;
    private EstadoInscripcion estado;
    private Integer bootcamp_id;
    private Integer employee_id;
}
