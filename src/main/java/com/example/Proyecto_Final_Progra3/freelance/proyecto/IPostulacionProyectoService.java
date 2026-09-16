package com.example.Proyecto_Final_Progra3.freelance.proyecto;

import java.util.List;

public interface IPostulacionProyectoService {
    PostulacionProyectoResponseDTO postularseMe(String username, Integer proyectoId, PostulacionProyectoRequestDTO dto);
    PostulacionProyectoResponseDTO postularse(Integer proyectoId, PostulacionProyectoRequestDTO dto);
    List<PostulacionProyectoResponseDTO> listarPostulacionesPorProyecto(Integer proyectoId);
}
