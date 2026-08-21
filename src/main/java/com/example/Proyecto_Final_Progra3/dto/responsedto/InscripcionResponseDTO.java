package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.EstadoInscripcion;
import java.time.LocalDate;

public record InscripcionResponseDTO(
        Integer id,
        LocalDate inscripcion,
        EstadoInscripcion estado,
        Integer bootcamp_id,
        Integer employee_id
) {}
