package com.example.Proyecto_Final_Progra3.config.security;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

// @Validated: si JWT_SECRET falta o es débil, la app no arranca (en vez de fallar recién al primer login)
@Validated
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        @NotNull(message = "Falta jwt.secret: definí JWT_SECRET en el .env o como variable de entorno")
        @Size(min = 32, message = "jwt.secret debe tener al menos 32 caracteres (HMAC-SHA256 exige 256 bits)")
        @Pattern(regexp = "^(?!\\$\\{).*", message = "JWT_SECRET no está definido: definilo en el .env o como variable de entorno")
        String secret,
        @NotNull(message = "Falta jwt.expiration")
        Long expiration
) {}
