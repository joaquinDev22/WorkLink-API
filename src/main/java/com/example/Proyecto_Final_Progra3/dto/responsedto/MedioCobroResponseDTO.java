package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.freelance.enums.TipoMedioCobro;

import java.time.LocalDateTime;

public record MedioCobroResponseDTO(
        Long id,
        Long freelancerId,
        TipoMedioCobro tipo,
        String titular,
        String documentoTitular,
        String cbuCvu,
        String alias,
        String mercadoPagoEmail,
        LocalDateTime updatedAt
) {}
