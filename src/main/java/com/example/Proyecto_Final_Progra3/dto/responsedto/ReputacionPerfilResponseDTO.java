package com.example.Proyecto_Final_Progra3.dto.responsedto;

import java.util.List;

public record ReputacionPerfilResponseDTO(
        Integer perfilId,
        Double promedio,
        Integer totalResenas,
        List<CalificacionProyectoResponseDTO> resenas
) {}

