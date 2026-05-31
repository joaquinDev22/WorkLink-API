package com.example.Proyecto_Final_Progra3.DTO.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaRequestDTO {
    private String nombre;
    private String descripcion;
    private String sitio_web;
    private String ubicacion;
    private int prefil_id;
}
