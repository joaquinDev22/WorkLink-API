package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LogginRequestDTO(
        @NotNull @NotBlank(message = "El usuario no puede estar vacio") String usuario,
        @NotNull @NotBlank(message = "La contrasena no puede estar vacia") String password
) {}
