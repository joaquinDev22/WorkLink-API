package com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.ProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ProyectoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IProyectoService {
    ProyectoResponseDTO crearProyecto(ProyectoRequestDTO dto);
    ProyectoResponseDTO crearMiProyecto(String username, ProyectoRequestDTO dto);
    void cancelarMiProyecto(Integer id, String username);
    ProyectoResponseDTO buscarPorId(Integer id);
    ProyectoResponseDTO buscarPorUuid(UUID uuid);
    List<ProyectoResponseDTO> listarTodos();
    List<ProyectoResponseDTO> listarMisProyectosPublicados(String username);
    List<ProyectoResponseDTO> listarMisProyectosAsignados(String username);
    ProyectoResponseDTO actualizarMiProyecto(Integer id, String username, ProyectoRequestDTO dto);
    ProyectoResponseDTO asignarFreelancer(Integer proyectoId, Integer employeeId, String username);
}
