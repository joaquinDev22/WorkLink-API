package com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.HitoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.HitoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoHitoProyecto;

import java.util.List;

public interface IHitoService {
    HitoResponseDTO crearHito(Integer proyectoId, HitoRequestDTO dto, String username);
    List<HitoResponseDTO> listarHitosPorProyecto(Integer proyectoId);
    HitoResponseDTO cambiarEstadoHito(Integer proyectoId, Integer hitoId, EstadoHitoProyecto nuevoEstado, String username);
}

