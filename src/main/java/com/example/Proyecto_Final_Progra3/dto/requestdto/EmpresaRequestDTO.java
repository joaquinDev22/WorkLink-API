package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmpresaRequestDTO(
        @NotBlank(message = "El nombre de la empresa no puede estar vacio") String nombre,
        @NotBlank(message = "La descripcion de la empresa no puede estar vacia")
        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres") String descripcion,
        String sitio_web,
        String ubicacion,
        @Size(max = 150, message = "La dirección no puede superar los 150 caracteres") String direccion,
        @Size(max = 30, message = "El teléfono no puede superar los 30 caracteres") String telefono,
        @Size(max = 100, message = "El rubro no puede superar los 100 caracteres") String rubro,
        Integer perfil_id
) {}
