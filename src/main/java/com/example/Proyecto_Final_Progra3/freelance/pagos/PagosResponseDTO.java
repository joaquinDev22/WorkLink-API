package com.example.Proyecto_Final_Progra3.freelance.pagos;

public record PagosResponseDTO(
        Integer pagoId,
        Moneda moneda,
        Integer monto,
        MedioDePago medioDePago,
        Integer proyectoId
) {}
