package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.ProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.service.freelance.ProyectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/freelance/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> crearProyecto(@Valid @RequestBody ProyectoRequestDTO dto) {
        return ResponseEntity.ok(proyectoService.crearProyecto(dto));
    }

    @PostMapping("/me")
    public ResponseEntity<ProyectoResponseDTO> crearMiProyecto(
            org.springframework.security.core.Authentication authentication, 
            @Valid @RequestBody ProyectoRequestDTO dto) {
        return ResponseEntity.ok(proyectoService.crearMiProyecto(authentication.getName(), dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(proyectoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ProyectoResponseDTO>> listarProyectos() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    @GetMapping("/me/publicados")
    public ResponseEntity<List<ProyectoResponseDTO>> listarMisProyectosPublicados(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(proyectoService.listarMisProyectosPublicados(authentication.getName()));
    }

    @GetMapping("/me/asignados")
    public ResponseEntity<List<ProyectoResponseDTO>> listarMisProyectosAsignados(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(proyectoService.listarMisProyectosAsignados(authentication.getName()));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarMiProyecto(@PathVariable Integer id, org.springframework.security.core.Authentication authentication) {
        proyectoService.cancelarMiProyecto(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
