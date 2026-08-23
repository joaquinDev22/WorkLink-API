package com.example.Proyecto_Final_Progra3.dto.responsedto;

public record LogginResponseDTO(
        String mensaje,
        String nombreUsuario,
        String rol,
        String token
) {}
