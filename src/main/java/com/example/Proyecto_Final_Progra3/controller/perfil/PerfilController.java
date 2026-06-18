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
    @PostMapping("/me/foto")
    public ResponseEntity<String> uploadFoto(Authentication authentication, @RequestParam("file") MultipartFile file) {
        try {
            perfilUsuarioService.uploadFoto(authentication.getName(), file);
            return ResponseEntity.ok("Foto de perfil subida exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir la foto: " + e.getMessage());
        }
    }

    @PostMapping("/me/banner")
    public ResponseEntity<String> uploadBanner(Authentication authentication, @RequestParam("file") MultipartFile file) {
        try {
            perfilUsuarioService.uploadBanner(authentication.getName(), file);
            return ResponseEntity.ok("Banner subido exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir el banner: " + e.getMessage());
        }
    }

    @PostMapping("/me/habilidades")
    public ResponseEntity<?> addHabilidad(Authentication authentication, 
                                          @RequestParam("nombre") String nombre, 
                                          @RequestParam("nivel") Integer nivel) {
        try {
            return ResponseEntity.ok(perfilUsuarioService.addHabilidad(authentication.getName(), nombre, nivel));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/me/habilidades/{habilidadId}")
    public ResponseEntity<?> removeHabilidad(Authentication authentication, @PathVariable Integer habilidadId) {
        try {
            perfilUsuarioService.removeHabilidad(authentication.getName(), habilidadId);
            return ResponseEntity.ok("Habilidad eliminada.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/me/proyectos")
    public ResponseEntity<?> addProyecto(Authentication authentication,
                                         @RequestParam("nombre") String nombre,
                                         @RequestParam("descripcion") String descripcion,
                                         @RequestParam(value = "link", required = false) String link,
                                         @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        try {
            return ResponseEntity.ok(perfilUsuarioService.addProyecto(authentication.getName(), nombre, descripcion, link, imagen));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/me/proyectos/{proyectoId}")
    public ResponseEntity<?> removeProyecto(Authentication authentication, @PathVariable Integer proyectoId) {
        try {
            perfilUsuarioService.removeProyecto(authentication.getName(), proyectoId);
            return ResponseEntity.ok("Proyecto eliminado.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
