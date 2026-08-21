package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.Moneda;

public record PagosResponseDTO(
        Integer pagoId,
        Moneda moneda,
        Integer monto,
        MedioDePago medioDePago,
        Integer proyectoId
) {}
