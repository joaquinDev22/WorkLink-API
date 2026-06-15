package com.example.Proyecto_Final_Progra3.controller;

import com.example.Proyecto_Final_Progra3.dto.requestdto.LogginRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.requestdto.PerfilRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.LogginResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.service.perfil.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<PerfilResponseDTO> registrar(@Valid @RequestBody PerfilRequestDTO perfil) {
        return new ResponseEntity<>(authService.register(perfil), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LogginResponseDTO> login(@Valid @RequestBody LogginRequestDTO dto) {
        return ResponseEntity.ok(authService.loggin(dto));
    }
}
