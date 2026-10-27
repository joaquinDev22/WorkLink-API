package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record HitoRequestDTO(
        @NotBlank(message = "El título del hito es obligatorio")
        @Size(max = 100, message = "El título no puede superar los 100 caracteres")
        String titulo,

        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        String descripcion,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal monto,

        LocalDate fechaLimite
) {}

