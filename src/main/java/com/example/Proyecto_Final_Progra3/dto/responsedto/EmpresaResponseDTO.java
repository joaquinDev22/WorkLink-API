package com.example.Proyecto_Final_Progra3.dto.responsedto;

import java.util.UUID;

public record EmpresaResponseDTO(
        Integer id,
        UUID uuid,
        String nombre,
        String direccion,
        String sitio_web,
        String ubicacion,
        Integer perfil_id
) {}

