package com.example.Proyecto_Final_Progra3.dto.requestdto;

import com.example.Proyecto_Final_Progra3.model.enums.Modalidad;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class BootcampEmpresaRequestDTO {
    private String titulo;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Modalidad modalidad;
    private Integer cupos;
    private Integer empresaId;
    private Integer instructorId;
}
