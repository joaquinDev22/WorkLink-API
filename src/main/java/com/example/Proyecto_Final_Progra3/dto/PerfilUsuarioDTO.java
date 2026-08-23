package com.example.Proyecto_Final_Progra3.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    // Instructor specific
    private String tipoInstructor;

    // Common profile extensions
    private byte[] fotoPerfil;
    private byte[] bannerPerfil;
    
    private java.util.List<HabilidadDTO> habilidades;
    private java.util.List<ProyectoDTO> proyectos;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HabilidadDTO {
        private Integer habilidadId;
        private String nombre;
        private Integer nivel; // 1: Básico, 2: Intermedio, 3: Avanzado, 4: Experto
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProyectoDTO {
        private Integer id;
        private String nombre;
        private String descripcion;
        private String link;
        private byte[] imagen;
    }
}

