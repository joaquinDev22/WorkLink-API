package com.example.Proyecto_Final_Progra3.bootcamps.instructor;

import com.example.Proyecto_Final_Progra3.bootcamps.bootcamp.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InstructorRequestDTO(
        @NotBlank(message = "El nombre del instructor no puede estar vacio") String nombre,
        @NotBlank(message = "El apellido del instructor no puede estar vacio") String apellido,
        @NotBlank(message = "El título profesional es obligatorio") String titulo,
        @NotBlank(message = "La presentación del instructor es obligatoria") String presentacion,
        @NotNull(message = "El tipo de instructor es obligatorio") Tipo tipo,
        Integer perfil_id,
        Integer empresa_id
) {}

