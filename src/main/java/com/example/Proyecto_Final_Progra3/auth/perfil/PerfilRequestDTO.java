package com.example.Proyecto_Final_Progra3.auth.perfil;

public record PerfilRequestDTO(
        String usuario,
        String email,
        String password,
        String rol
) {}
