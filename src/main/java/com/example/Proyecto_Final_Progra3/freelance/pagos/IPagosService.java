package com.example.Proyecto_Final_Progra3.freelance.pagos;

import java.util.List;

public interface IPagosService {
    PagosResponseDTO registrarPago(Integer proyectoId, PagosRequestDTO dto);
    List<PagosResponseDTO> listarPagos(Integer proyectoId);
}
