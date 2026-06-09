package com.example.Proyecto_Final_Progra3.dto.requestdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaRequestDTO {
    private String nombre;
    private String descripcion;
    private String sitio_web;
    private String ubicacion;
    private Integer prefil_id;
}
