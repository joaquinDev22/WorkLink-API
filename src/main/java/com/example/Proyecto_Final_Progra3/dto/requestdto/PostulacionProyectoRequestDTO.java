package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PostulacionProyectoRequestDTO(
        @NotBlank(message = "La descripción/propuesta es obligatoria") String descripcion,
        @NotNull(message = "El costo propuesto es obligatorio")
        @DecimalMin(value = "0.01", message = "El costo propuesto debe ser mayor a 0") BigDecimal costo,
        @NotNull(message = "El ID del freelancer (employee) es obligatorio") Integer employeeId
) {}
