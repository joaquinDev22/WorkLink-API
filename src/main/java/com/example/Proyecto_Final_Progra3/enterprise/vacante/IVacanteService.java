package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import java.util.List;

public interface IVacanteService {
    VacanteResponseDTO crearVacante(VacanteRequestDTO dto);
    VacanteResponseDTO crearMiVacante(String username, VacanteRequestDTO dto);
    VacanteResponseDTO buscarPorId(Integer id);
    List<VacanteResponseDTO> listarTodas();
    List<VacanteResponseDTO> listarMisVacantes(String username);
}
