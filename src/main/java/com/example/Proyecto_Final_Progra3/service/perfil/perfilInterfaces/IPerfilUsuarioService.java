package com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces;

import com.example.Proyecto_Final_Progra3.dto.PerfilUsuarioDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IPerfilUsuarioService {
    PerfilUsuarioDTO getMyProfile(String username);
    PerfilUsuarioDTO updateMyProfile(String username, PerfilUsuarioDTO request);
    void uploadFoto(String username, MultipartFile file) throws IOException;
    void uploadBanner(String username, MultipartFile file) throws IOException;
    PerfilUsuarioDTO.HabilidadDTO addHabilidad(String username, String nombreHabilidad, Integer nivel);
    void removeHabilidad(String username, Integer habilidadId);
    PerfilUsuarioDTO.ProyectoDTO addProyecto(String username, String nombre, String descripcion, String link, MultipartFile imagen) throws IOException;
    void removeProyecto(String username, Integer proyectoId);
    void uploadMyCV(String username, MultipartFile file);
    byte[] getMyCV(String username);
    void desactivarMiPerfil(String username);
}
