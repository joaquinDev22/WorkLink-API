package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import java.time.LocalDate;

public record InscripcionResponseDTO(
        Integer id,
        LocalDate inscripcion,
        EstadoInscripcion estado,
        Integer bootcamp_id,
        Integer employee_id
) {}
