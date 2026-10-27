package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EntrevistaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EntrevistaResponseDTO;

import java.util.List;

public interface IEntrevistaService {
    List<EntrevistaResponseDTO> listarEntrevistas();
    List<EntrevistaResponseDTO> listarMisEntrevistas(String username);
    EntrevistaResponseDTO agendarEntrevista(EntrevistaRequestDTO dto);
}
