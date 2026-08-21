package com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PagosRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;

import java.util.List;

public interface IPagosService {
    PagosResponseDTO registrarPago(Integer proyectoId, PagosRequestDTO dto);
    List<PagosResponseDTO> listarPagos(Integer proyectoId);
}
