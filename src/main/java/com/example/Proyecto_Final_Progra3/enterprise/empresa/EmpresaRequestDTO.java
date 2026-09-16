package com.example.Proyecto_Final_Progra3.enterprise.empresa;

import jakarta.validation.constraints.NotBlank;

public record EmpresaRequestDTO(
        @NotBlank(message = "El nombre de la empresa no puede estar vacio") String nombre,
        @NotBlank(message = "La descripcion de la empresa no puede estar vacia") String descripcion,
        String sitio_web,
        String ubicacion,
        Integer perfil_id
) {}
