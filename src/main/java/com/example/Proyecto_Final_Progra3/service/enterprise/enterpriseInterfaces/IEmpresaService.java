package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;

import java.util.List;
import java.util.Map;

public interface IEmpresaService {
    EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO dto);
    EmpresaResponseDTO editarEmpresa(Integer id, EmpresaRequestDTO dto);
    List<EmpresaResponseDTO> mostrarEmpresas();
    EmpresaResponseDTO buscarEmpresaXid(Integer id);
    List<EmpresaResponseDTO> buscarEmpresasXidPerfil(int idPerfil);
    void eliminarEmpresa(Integer idEmpresa);
    Map<String, Long> getDashboardStats(String username);
}
