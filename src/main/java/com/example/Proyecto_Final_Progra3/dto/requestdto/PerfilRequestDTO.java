package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PerfilRequestDTO(
        @NotBlank(message = "El usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
        String usuario,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        // RF01: los 5 roles registrables. INSTRUCTOR queda fuera (bootcamps diferido, ADR-002)
        @NotBlank(message = "El rol es obligatorio")
        @Pattern(regexp = "POSTULANTE|FREELANCER|EMPRESA|RECRUITER|CLIENTE",
                message = "El rol debe ser POSTULANTE, FREELANCER, EMPRESA, RECRUITER o CLIENTE")
        String rol
) {}
