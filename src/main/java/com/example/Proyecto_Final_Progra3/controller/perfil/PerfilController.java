package com.example.Proyecto_Final_Progra3.controller.perfil;

import com.example.Proyecto_Final_Progra3.dto.PerfilUsuarioDTO;
import com.example.Proyecto_Final_Progra3.service.perfil.PerfilUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/perfiles")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilUsuarioService perfilUsuarioService;

    @GetMapping("/me")
    public ResponseEntity<PerfilUsuarioDTO> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(perfilUsuarioService.getMyProfile(username));
    }

    @PutMapping("/me")
    public ResponseEntity<PerfilUsuarioDTO> updateMyProfile(Authentication authentication, @RequestBody PerfilUsuarioDTO request) {
        String username = authentication.getName();
        return ResponseEntity.ok(perfilUsuarioService.updateMyProfile(username, request));
    }

    @PostMapping("/me/cv")
    public ResponseEntity<String> uploadMyCV(Authentication authentication, @RequestParam("file") MultipartFile file) {
        String username = authentication.getName();
        try {
            perfilUsuarioService.uploadMyCV(username, file);
            return ResponseEntity.ok("CV subido exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir el CV: " + e.getMessage());
        }
    }
}
