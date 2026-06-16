package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VacanteRequestDTO {

    @NotBlank(message = "El puesto es obligatorio")
    private String puesto;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La fecha de finalización es obligatoria")
    @Future(message = "La fecha de finalización debe ser futura")
    private LocalDate finalizacion;

    @NotNull(message = "El id de la empresa no puede ser nulo")
    private Integer empresaId;

    private String logo;
    private String ubicacion;
    private String tipoUbicacion;
    private String tipoVacante;
    private String salario;
    private java.util.List<String> etiquetas;
}
