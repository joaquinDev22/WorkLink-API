package com.example.Proyecto_Final_Progra3.dto.responsedto;

import java.time.LocalDate;
import java.util.List;

public record VacanteResponseDTO(
        Integer vacanteId,
        String puesto,
        String descripcion,
        LocalDate publicacion,
        LocalDate finalizacion,
        Integer empresaId,
        String nombreEmpresa,
        String logo,
        String ubicacion,
        String tipoUbicacion,
        String tipoVacante,
        String salario,
        List<String> etiquetas
) {}
