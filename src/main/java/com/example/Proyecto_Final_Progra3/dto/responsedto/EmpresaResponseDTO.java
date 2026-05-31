package com.example.Proyecto_Final_Progra3.DTO.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaResponseDTO {
    private int id;
    private String nombre;
    private String direccion;
    private String sitio_web;
    private String ubicacion;
    private int perfil_id;
}
