package com.example.Proyecto_Final_Progra3.service.freelance.strategy;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PagosRequestDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import com.example.Proyecto_Final_Progra3.model.freelance.pagos.PagosEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;

/**
 * Patrón GoF Strategy + SOLID OCP (Open/Closed Principle).
 * Define el contrato algorítmico para el procesamiento y estructuración
 * de pagos según el medio seleccionado, permitiendo incorporar nuevos medios
 * de pago sin modificar el código de los servicios existentes.
 */
public interface IPagoStrategy {

    /**
     * Determina si esta estrategia maneja el medio de pago indicado.
     * @param medioDePago Enum del medio de pago solicitado
     * @return true si la estrategia soporta este medio
     */
    boolean supports(MedioDePago medioDePago);

    /**
     * Procesa y construye la entidad de pago asociada al proyecto con las reglas
     * de validación correspondientes al medio de pago específico.
     * @param proyecto Entidad del proyecto al que se asocia el pago
     * @param dto Datos del pago solicitados
     * @return Entidad de pago lista para ser persistida
     */
    PagosEntity procesarPago(ProyectoEntity proyecto, PagosRequestDTO dto);
}

