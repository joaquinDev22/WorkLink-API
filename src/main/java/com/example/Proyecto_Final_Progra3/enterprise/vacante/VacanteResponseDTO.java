package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record VacanteResponseDTO(
        Integer vacanteId,
        UUID uuid,
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

