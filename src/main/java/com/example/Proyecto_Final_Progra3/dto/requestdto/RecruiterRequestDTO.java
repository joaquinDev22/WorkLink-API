package com.example.Proyecto_Final_Progra3.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// La empresa vincula al recruiter solo por email: el resto de los datos ya están en su cuenta de WorkLink
@Schema(description = "Datos para vincular un recruiter a la empresa")
public record RecruiterRequestDTO(
        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "Debe ser un correo electrónico válido")
        @Size(max = 100, message = "El email no puede superar los 100 caracteres")
        @Schema(description = "Email con el que el recruiter se registró en WorkLink", example = "maria.gonzalez@empresa.com")
        String email
) {}
