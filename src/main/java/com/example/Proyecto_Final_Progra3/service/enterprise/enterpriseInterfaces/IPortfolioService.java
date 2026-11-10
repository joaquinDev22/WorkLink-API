package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces;

import com.example.Proyecto_Final_Progra3.dto.PerfilUsuarioDTO;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IPortfolioService {
    void guardarCV(Integer employeeId, String username, MultipartFile file);
    PerfilUsuarioDTO.ProyectoDTO addProyecto(PerfilEntity perfil, String nombre, String descripcion, String link, MultipartFile imagen) throws IOException;
    void removeProyecto(PerfilEntity perfil, Integer proyectoId);
}
