package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.DashboardStatsResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import java.util.List;
import java.util.UUID;

public interface IEmpresaService {
    EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO dto);
    EmpresaResponseDTO editarEmpresa(Integer id, String username, EmpresaRequestDTO dto);
    List<EmpresaResponseDTO> mostrarEmpresas();
    EmpresaResponseDTO buscarEmpresaXid(Integer id);
    EmpresaResponseDTO buscarEmpresaXuuid(UUID uuid);
    List<EmpresaResponseDTO> buscarEmpresasXidPerfil(int idPerfil);
    void eliminarEmpresa(Integer idEmpresa, String username);
    DashboardStatsResponseDTO getDashboardStats(String username);
}
