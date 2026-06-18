package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProyectoRequestDTO {

    @NotBlank(message = "El área del proyecto es obligatoria")
    private String area;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El presupuesto máximo es obligatorio")
    private BigDecimal presupuestoMax;

    @NotNull(message = "El presupuesto mínimo es obligatorio")
    private BigDecimal presupuestoMin;

    @NotNull(message = "El cliente ID no puede ser nulo")
    private Integer clienteId;
    
    private String duracion;
    private String tipoPago;
    private java.util.List<String> habilidades;
}
