package com.example.Proyecto_Final_Progra3.auth.login;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilRequestDTO;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilResponseDTO;

public interface IAuthService {
    PerfilResponseDTO register(PerfilRequestDTO dto);
    LoginResponseDTO loggin(LoginRequestDTO dto);
    void bajaPerfil(PerfilRequestDTO dto);
    void altaPerfil(PerfilRequestDTO dto);
}
