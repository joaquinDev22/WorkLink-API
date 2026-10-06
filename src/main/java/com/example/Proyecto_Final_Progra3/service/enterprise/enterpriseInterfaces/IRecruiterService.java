package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.RecruiterRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.RecruiterResponseDTO;

import java.util.List;

public interface IRecruiterService {
    RecruiterResponseDTO crearRecruiter(String usernameEmpresa, RecruiterRequestDTO dto);
    List<RecruiterResponseDTO> listarRecruitersPorEmpresa(String usernameEmpresa);
    RecruiterResponseDTO buscarPorId(String usernameEmpresa, Integer recruiterId);
    RecruiterResponseDTO actualizarRecruiter(String usernameEmpresa, Integer recruiterId, RecruiterRequestDTO dto);
    void eliminarRecruiter(String usernameEmpresa, Integer recruiterId);
}

