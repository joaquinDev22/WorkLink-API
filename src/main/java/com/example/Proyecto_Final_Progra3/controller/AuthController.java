package com.example.Proyecto_Final_Progra3.controller;

import com.example.Proyecto_Final_Progra3.dto.requestdto.LogginRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.requestdto.PerfilRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.LogginResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.service.perfil.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
