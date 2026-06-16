package com.example.Proyecto_Final_Progra3.dto.responsedto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProyectoResponseDTO {
    private Integer proyectoId;
    private String area;
    private String descripcion;
    private LocalDate fechaPublicacion;
    private Integer presupuestoMax;
    private Integer presupuestoMin;
    private Integer clienteId;
    private String nombreCliente;
    
    private String duracion;
    private String tipoPago;
    private java.util.List<String> habilidades;
}
