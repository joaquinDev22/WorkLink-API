package com.example.Proyecto_Final_Progra3.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InstructorEmpresaRequestDTO {
    private String nombre;
    private String titulo;
    private String descripcion;
    private Integer empresaId;
}
