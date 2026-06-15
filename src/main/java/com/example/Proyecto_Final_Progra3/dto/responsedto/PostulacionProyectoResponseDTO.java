package com.example.Proyecto_Final_Progra3.dto.responsedto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostulacionProyectoResponseDTO {
    private Integer postulacionId;
    private String descripcion;
    private Double costoPropuesto;
    private LocalDate fechaPostulacion;
    private Integer empleadoId;
    private String nombreEmpleado;
    private Integer proyectoId;
}
