package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;

public record InstructorResponseDTO(
        Integer id,
        String nombre,
        String titulo,
        String presentacion,
        Tipo tipo,
        Integer empresa_id
) {}
