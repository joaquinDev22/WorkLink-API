package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaRequestDTO {
    @NotBlank(message = "El nombre de la empresa no puede estar vacio")
    private String nombre;
    @NotBlank(message = "La descripcion de la empresa no puede estar vacia")
    private String descripcion;
    private String sitio_web;
    private String ubicacion;
    private Integer prefil_id;
}
