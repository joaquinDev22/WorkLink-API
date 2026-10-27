package com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.CalificacionProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.CalificacionProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ReputacionPerfilResponseDTO;

import java.util.List;

public interface ICalificacionProyectoService {
    CalificacionProyectoResponseDTO calificarProyecto(Integer proyectoId, CalificacionProyectoRequestDTO dto, String username);
    List<CalificacionProyectoResponseDTO> listarCalificacionesPorProyecto(Integer proyectoId);
    ReputacionPerfilResponseDTO obtenerReputacionPerfil(Integer perfilId);
}

