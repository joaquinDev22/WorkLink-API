package com.example.Proyecto_Final_Progra3.dto;

import lombok.Data;

@Data
public class PerfilUsuarioDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    
    // Employee specific
    private String fechaNacimiento;
    
    // Client & Empresa specific
    private String telefono;
    private String pais;
    
    // Empresa specific
    private String descripcion;
    private String ubicacion;
    private String sitioWeb;
    private String logo;
}
