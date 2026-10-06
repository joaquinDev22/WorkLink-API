package com.example.Proyecto_Final_Progra3.controller;

import com.example.Proyecto_Final_Progra3.dto.requestdto.LoginRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.requestdto.PerfilRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.LoginResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.requestdto.GoogleLoginRequestDTO;
import com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces.IGoogleAuthService;
import com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "Operaciones relacionadas con el registro, inicio de sesión y autenticación de usuarios.")

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final IGoogleAuthService googleAuthService;

    @Operation(summary = "Registrar usuario", description = "Permite registrar un nuevo usuario en la plataforma creando su perfil y credenciales de acceso.")
    @PostMapping("/register")
    public ResponseEntity<PerfilResponseDTO> registrar(@Valid @RequestBody PerfilRequestDTO perfil) {
        return new ResponseEntity<>(authService.register(perfil), HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica a un usuario mediante sus credenciales y devuelve la información necesaria para acceder a los recursos protegidos.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.loggin(dto));
    }

    @Operation(summary = "Autenticación con Google", description = "Valida el idToken criptográfico emitido por Google Identity Services y devuelve un JWT nativo de WorkLink.")
    @PostMapping("/google")
    public ResponseEntity<LoginResponseDTO> loginGoogle(@Valid @RequestBody GoogleLoginRequestDTO dto) {
        return ResponseEntity.ok(googleAuthService.authenticateGoogle(dto));
    }
}
