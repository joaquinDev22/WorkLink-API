package com.example.Proyecto_Final_Progra3.dto.responsedto;

public record LoginResponseDTO(
        String mensaje,
        String nombreUsuario,
        String rol,
        String token
) {}
