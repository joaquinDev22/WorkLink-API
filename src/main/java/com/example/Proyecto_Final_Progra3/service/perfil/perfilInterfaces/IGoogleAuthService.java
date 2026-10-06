package com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.GoogleLoginRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.LoginResponseDTO;

public interface IGoogleAuthService {
    LoginResponseDTO authenticateGoogle(GoogleLoginRequestDTO request);
}

