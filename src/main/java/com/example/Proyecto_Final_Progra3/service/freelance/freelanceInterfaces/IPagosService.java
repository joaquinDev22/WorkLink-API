package com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces;

import com.example.Proyecto_Final_Progra3.dto.responsedto.MercadoPagoPreferenceResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;
import java.util.List;

public interface IPagosService {
    List<PagosResponseDTO> listarPagos(Integer proyectoId, String username);
    MercadoPagoPreferenceResponseDTO crearPreferenciaPago(Integer proyectoId, Integer hitoId, String username);
    void procesarWebhook(String topic, String id, String type, String dataId);
}
