package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostulacionProyectoRequestDTO {

    @NotBlank(message = "La descripción/propuesta es obligatoria")
    private String descripcion;

    @NotNull(message = "El costo propuesto es obligatorio")
    private Double costo;

    @NotNull(message = "El ID del freelancer (employee) es obligatorio")
    private Integer employeeId;
}
