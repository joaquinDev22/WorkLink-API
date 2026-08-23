package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record ProyectoRequestDTO(
        @NotBlank(message = "El área del proyecto es obligatoria") String area,
        @NotBlank(message = "La descripción es obligatoria") String descripcion,
        @NotNull(message = "El presupuesto máximo es obligatorio") @DecimalMin(value = "0.0", message = "El presupuesto máximo debe ser un valor positivo") BigDecimal presupuestoMax,
        @NotNull(message = "El presupuesto mínimo es obligatorio") @DecimalMin(value = "0.0", message = "El presupuesto mínimo debe ser un valor positivo") BigDecimal presupuestoMin,
        Integer clienteId,
        String duracion,
        String tipoPago,
        List<String> habilidades
) {}

