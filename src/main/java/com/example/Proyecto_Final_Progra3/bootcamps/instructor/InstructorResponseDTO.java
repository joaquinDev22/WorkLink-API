package com.example.Proyecto_Final_Progra3.bootcamps.instructor;

import com.example.Proyecto_Final_Progra3.bootcamps.bootcamp.Tipo;

public record InstructorResponseDTO(
        Integer id,
        String nombre,
        String titulo,
        String presentacion,
        Tipo tipo,
        Integer empresa_id
) {}
