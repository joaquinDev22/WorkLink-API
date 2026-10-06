package com.example.Proyecto_Final_Progra3.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Datos para crear o actualizar un recruiter asociado a una empresa")
public record RecruiterRequestDTO(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Schema(description = "Nombre del recruiter", example = "María")
        String nombre,

        @NotBlank(message = "El apellido no puede estar vacío")
        @Schema(description = "Apellido del recruiter", example = "González")
        String apellido,

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "Debe ser un correo electrónico válido")
        @Schema(description = "Correo electrónico institucional del recruiter", example = "maria.gonzalez@empresa.com")
        String email,

        @NotNull(message = "La fecha de nacimiento no puede ser nula")
        @Schema(description = "Fecha de nacimiento del recruiter", example = "1992-05-15")
        LocalDate fechaNacimiento,

        @Schema(description = "Área de reclutamiento o departamento", example = "IT & Tech Recruiting")
        String area
) {}

