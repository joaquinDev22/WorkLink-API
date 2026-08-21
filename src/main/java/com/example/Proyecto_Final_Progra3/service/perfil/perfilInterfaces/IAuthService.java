package com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.LogginRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.requestdto.PerfilRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.LogginResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PerfilResponseDTO;

public interface IAuthService {
    PerfilResponseDTO register(PerfilRequestDTO dto);
    LogginResponseDTO loggin(LogginRequestDTO dto);
    void bajaPerfil(PerfilRequestDTO dto);
    void altaPerfil(PerfilRequestDTO dto);
}
