package com.example.Proyecto_Final_Progra3.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para autenticación con Google OAuth2 mediante idToken")
public record GoogleLoginRequestDTO(
        @NotNull(message = "El idToken no puede ser nulo")
        @NotBlank(message = "El idToken no puede estar vacío")
        @Schema(description = "Token de identidad JWT emitido por los servidores de Google Identity Services", example = "eyJhbGciOiJSUzI1NiIsImtpZCI6...")
        String idToken
) {}

