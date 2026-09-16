package com.example.Proyecto_Final_Progra3.auth.perfil;

public record PerfilResponseDTO(
        Integer perfil_id,
        String usuario,
        String email,
        String rol
) {}
