package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.Moneda;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagosResponseDTO(
        Integer pagoId,
        Moneda moneda,
        BigDecimal monto,
        MedioDePago medioDePago,
        Integer proyectoId,
        String mpPaymentId,  // identificador de la operación en Mercado Pago (RF30/RF31)
        LocalDateTime fecha  // WL-39
) {}
