package com.example.Proyecto_Final_Progra3.dto.responsedto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacanteResponseDTO {
    private Integer vacanteId;
    private String puesto;
    private String descripcion;
    private LocalDate publicacion;
    private LocalDate finalizacion;
    private Integer empresaId;
    private String nombreEmpresa;
}
