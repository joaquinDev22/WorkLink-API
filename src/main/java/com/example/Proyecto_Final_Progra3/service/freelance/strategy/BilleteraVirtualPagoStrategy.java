package com.example.Proyecto_Final_Progra3.service.freelance.strategy;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PagosRequestDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BadRequestException;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import com.example.Proyecto_Final_Progra3.model.freelance.pagos.PagosEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Estrategia concreta para el procesamiento de pagos mediante Billetera Virtual (Mercado Pago u otras).
 */
@Slf4j
@Component
public class BilleteraVirtualPagoStrategy implements IPagoStrategy {

    @Override
    public boolean supports(MedioDePago medioDePago) {
        return MedioDePago.BILLETERA_VIRTUAL.equals(medioDePago);
    }

    @Override
    public PagosEntity procesarPago(ProyectoEntity proyecto, PagosRequestDTO dto) {
        if (dto.monto() == null || dto.monto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El monto a procesar con billetera virtual debe ser superior a 0.");
        }

        log.info("Aplicando estrategia BILLETERA_VIRTUAL para el proyecto ID: {}, monto: {} {}",
                proyecto.getId(), dto.monto(), dto.moneda());

        return PagosEntity.builder()
                .moneda(dto.moneda())
                .monto(dto.monto())
                .medioDePago(MedioDePago.BILLETERA_VIRTUAL)
                .proyecto(proyecto)
                .build();
    }
}

