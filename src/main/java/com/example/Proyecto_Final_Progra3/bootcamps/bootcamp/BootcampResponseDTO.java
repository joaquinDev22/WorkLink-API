package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import java.time.LocalDate;
import java.util.List;

public record BootcampResponseDTO(
        Integer id,
        String titulo,
        String descripcion,
        LocalDate inicio,
        LocalDate fin,
        Modalidad modalidad,
        Integer cupos,
        Tipo tipo,
        Integer instructor_id,
        String logo,
        String precio,
        Double rating,
        List<String> etiquetas
) {}
