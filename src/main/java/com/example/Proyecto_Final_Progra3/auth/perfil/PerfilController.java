package com.example.Proyecto_Final_Progra3.auth.perfil;

import com.example.Proyecto_Final_Progra3.shared.dto.MessageResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/perfiles")
@RequiredArgsConstructor
@Tag(name = "Perfiles", description = "Administración de perfiles de usuario y datos personales.")
public class PerfilController {

    private final IPerfilUsuarioService perfilUsuarioService;

    @Operation(summary = "Obtener mi perfil", description = "Obtiene la información completa del perfil del usuario autenticado.")
    @GetMapping("/getProfile")
    public ResponseEntity<PerfilUsuarioDTO> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(perfilUsuarioService.getMyProfile(username));
    }

    @Operation(summary = "Actualizar mi perfil", description = "Permite modificar la información personal y profesional del usuario autenticado.")
    @PutMapping("/updateProfile")
    public ResponseEntity<PerfilUsuarioDTO> updateMyProfile(Authentication authentication, @RequestBody PerfilUsuarioDTO request) {
        String username = authentication.getName();
        return ResponseEntity.ok(perfilUsuarioService.updateMyProfile(username, request));
    }

    @Operation(summary = "Subir mi CV", description = "Permite cargar o actualizar el currículum vitae del usuario autenticado.")
    @PostMapping("/uploadMyCV")
    public ResponseEntity<MessageResponseDTO> uploadMyCV(Authentication authentication, @RequestParam("file") MultipartFile file) {
        String username = authentication.getName();
        perfilUsuarioService.uploadMyCV(username, file);
        return ResponseEntity.ok(new MessageResponseDTO("CV subido exitosamente a la base de datos."));
    }

    @Operation(summary = "Subir foto de perfil", description = "Permite cargar o actualizar la foto de perfil del usuario autenticado.")
    @PostMapping("/uploadMyPhoto")
    public ResponseEntity<MessageResponseDTO> uploadFoto(Authentication authentication, @RequestParam("file") MultipartFile file) throws IOException {
        perfilUsuarioService.uploadFoto(authentication.getName(), file);
        return ResponseEntity.ok(new MessageResponseDTO("Foto de perfil subida exitosamente."));
    }

    @Operation(summary = "Subir banner", description = "Permite cargar o actualizar la imagen de banner del perfil del usuario autenticado.")
    @PostMapping("/uploadMyBanner")
    public ResponseEntity<MessageResponseDTO> uploadBanner(Authentication authentication, @RequestParam("file") MultipartFile file) throws IOException {
        perfilUsuarioService.uploadBanner(authentication.getName(), file);
        return ResponseEntity.ok(new MessageResponseDTO("Banner subido exitosamente."));
    }

    @Operation(summary = "Agregar habilidad", description = "Permite agregar una nueva habilidad al perfil del usuario autenticado indicando su nivel de dominio.")
    @PostMapping("/addMySkill")
    public ResponseEntity<MessageResponseDTO> addHabilidad(Authentication authentication, @RequestParam("nombre") String nombre, @RequestParam("nivel") Integer nivel) {
            perfilUsuarioService.addHabilidad(authentication.getName(), nombre, nivel);
            return ResponseEntity.ok(new MessageResponseDTO("Habilidad agregada exitosamente."));
    }

    @Operation(summary = "Eliminar habilidad", description = "Permite eliminar una habilidad previamente asociada al perfil del usuario autenticado.")
    @DeleteMapping("/removeMySkill/{habilidadId}")
    public ResponseEntity<MessageResponseDTO> removeHabilidad(Authentication authentication, @PathVariable Integer habilidadId) {
        perfilUsuarioService.removeHabilidad(authentication.getName(), habilidadId);
        return ResponseEntity.ok(new MessageResponseDTO("Habilidad eliminada exitosamente."));
    }

    @Operation(summary = "Agregar proyecto al portafolio", description = "Permite agregar un proyecto al portafolio profesional del usuario autenticado, incluyendo descripción, enlace e imagen opcional.")
    @PostMapping("/addMyProject")
    public ResponseEntity<MessageResponseDTO> addProyecto(Authentication authentication,
                                         @RequestParam("nombre") String nombre,
                                         @RequestParam("descripcion") String descripcion,
                                         @RequestParam(value = "link", required = false) String link,
                                         @RequestParam(value = "imagen", required = false) MultipartFile imagen) throws IOException {
    perfilUsuarioService.addProyecto(authentication.getName(), nombre, descripcion, link, imagen);
    return ResponseEntity.ok(new MessageResponseDTO("Proyecto agregado al portafolio exitosamente."));
}

    @Operation(summary = "Eliminar proyecto del portafolio", description = "Permite eliminar un proyecto previamente registrado en el portafolio del usuario autenticado.")
    @DeleteMapping("/removeMyProject/{proyectoId}")
    public ResponseEntity<MessageResponseDTO> removeProyecto(Authentication authentication, @PathVariable Integer proyectoId) {
        perfilUsuarioService.removeProyecto(authentication.getName(), proyectoId);
        return ResponseEntity.ok(new MessageResponseDTO("Proyecto eliminado."));
    }
}
