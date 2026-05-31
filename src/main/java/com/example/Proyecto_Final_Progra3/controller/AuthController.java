package com.example.Proyecto_Final_Progra3.controller;

import com.example.Proyecto_Final_Progra3.DTO.requestdto.LogginRequestDTO;
import com.example.Proyecto_Final_Progra3.DTO.requestdto.PerfilRequestDTO;
import com.example.Proyecto_Final_Progra3.DTO.responsedto.LogginResponseDTO;
import com.example.Proyecto_Final_Progra3.DTO.responsedto.PerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.service.perfil.AuthService;
import com.example.Proyecto_Final_Progra3.service.perfil.PerfilDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private  PasswordEncoder passwordEncoder;
    private final PerfilRepository perfilRepository;
    private final AuthService authService;

    @PostMapping("/register")
    public PerfilResponseDTO registrar(@RequestBody PerfilRequestDTO perfil) {
        return authService.register(perfil);
    }

    @PostMapping("/loggin")
    public LogginResponseDTO login(@RequestBody LogginRequestDTO dto) {
        return authService.loggin(dto);
    }

}
