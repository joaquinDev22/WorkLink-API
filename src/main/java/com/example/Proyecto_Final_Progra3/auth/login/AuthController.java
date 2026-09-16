package com.example.Proyecto_Final_Progra3.auth.login;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilRequestDTO;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilResponseDTO;

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
}
