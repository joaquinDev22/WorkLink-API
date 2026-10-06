package com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PagosRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;

import com.example.Proyecto_Final_Progra3.dto.responsedto.MercadoPagoPreferenceResponseDTO;
import java.math.BigDecimal;
import java.util.List;

public interface IPagosService {
    PagosResponseDTO registrarPago(Integer proyectoId, PagosRequestDTO dto);
    List<PagosResponseDTO> listarPagos(Integer proyectoId);
    MercadoPagoPreferenceResponseDTO crearPreferenciaPago(Integer proyectoId, BigDecimal monto, String backUrl);
    void procesarWebhook(String topic, String id, String type, String dataId);
}
