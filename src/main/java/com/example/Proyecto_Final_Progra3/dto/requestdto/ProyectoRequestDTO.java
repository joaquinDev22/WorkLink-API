package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record ProyectoRequestDTO(
        @NotBlank(message = "El nombre del proyecto es obligatorio") @Size(max = 30, message = "El nombre del proyecto no puede superar los 30 caracteres") String nombreProyecto,
        @NotBlank(message = "El área del proyecto es obligatoria") @Size(max = 10, message = "El área no puede superar los 10 caracteres") String area,
        @NotBlank(message = "La descripción es obligatoria") @Size(max = 255, message = "La descripción no puede superar los 255 caracteres") String descripcion,
        @NotNull(message = "El presupuesto máximo es obligatorio") @DecimalMin(value = "0.0", message = "El presupuesto máximo debe ser un valor positivo") BigDecimal presupuestoMax,
        @NotNull(message = "El presupuesto mínimo es obligatorio") @DecimalMin(value = "0.0", message = "El presupuesto mínimo debe ser un valor positivo") BigDecimal presupuestoMin,
        Integer clienteId,
        @Size(max = 50, message = "La duración no puede superar los 50 caracteres") String duracion,
        @Size(max = 50, message = "El tipo de pago no puede superar los 50 caracteres") String tipoPago,
        List<String> habilidades
) {}

