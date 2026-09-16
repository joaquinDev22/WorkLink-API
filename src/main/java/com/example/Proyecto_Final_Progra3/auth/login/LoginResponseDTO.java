package com.example.Proyecto_Final_Progra3.auth.login;

public record LoginResponseDTO(
        String mensaje,
        String nombreUsuario,
        String rol,
        String token
) {}
