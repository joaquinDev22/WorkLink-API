package com.example.Proyecto_Final_Progra3.dto.requestdto;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import jakarta.validation.constraints.NotBlank;

public record InstructorRequestDTO(
        @NotBlank(message = "El nombre del instructor no puede estar vacio") String nombre,
        @NotBlank(message = "El apellido del instructor no puede estar vacio") String apellido,
        String titulo,
        String presentacion,
        Tipo tipo,
        Integer perfil_id,
        Integer empresa_id
) {}
