package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.ProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.service.freelance.ProyectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/freelance/proyectos")
@RequiredArgsConstructor
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class ProyectoController {

    private final ProyectoService proyectoService;

    @Operation(summary = "Crear proyecto", description = "Permite registrar un nuevo proyecto freelance en la plataforma.")
    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> crearProyecto(@Valid @RequestBody ProyectoRequestDTO dto) {
        return ResponseEntity.ok(proyectoService.crearProyecto(dto));
    }

    @Operation(summary = "Crear mi proyecto", description = "Permite al cliente autenticado publicar un nuevo proyecto freelance asociado a su cuenta.")
    @PostMapping("/me")
    public ResponseEntity<ProyectoResponseDTO> crearMiProyecto(
            org.springframework.security.core.Authentication authentication, 
            @Valid @RequestBody ProyectoRequestDTO dto) {
        return ResponseEntity.ok(proyectoService.crearMiProyecto(authentication.getName(), dto));
    }

    @Parameter(name = "id", description = "Identificador único del proyecto freelance", example = "1")
    @Operation(summary = "Buscar proyecto por ID", description = "Obtiene la información detallada de un proyecto freelance a partir de su identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(proyectoService.buscarPorId(id));
    }

    @Operation(summary = "Listar proyectos", description = "Obtiene la lista completa de proyectos freelance publicados en la plataforma.")
    @GetMapping
    public ResponseEntity<List<ProyectoResponseDTO>> listarProyectos() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    @Operation(summary = "Listar mis proyectos publicados", description = "Obtiene todos los proyectos publicados por el cliente autenticado.")
    @GetMapping("/me/publicados")
    public ResponseEntity<List<ProyectoResponseDTO>> listarMisProyectosPublicados(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(proyectoService.listarMisProyectosPublicados(authentication.getName()));
    }

    @Operation(summary = "Listar mis proyectos asignados", description = "Obtiene todos los proyectos asignados al freelancer autenticado.")
    @GetMapping("/me/asignados")
    public ResponseEntity<List<ProyectoResponseDTO>> listarMisProyectosAsignados(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(proyectoService.listarMisProyectosAsignados(authentication.getName()));
    }

    @Parameter(name = "id", description = "Identificador único del proyecto freelance", example = "1")
    @Operation(summary = "Cancelar proyecto", description = "Permite al propietario del proyecto cancelar un proyecto freelance publicado.")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarMiProyecto(@PathVariable Integer id, org.springframework.security.core.Authentication authentication) {
        proyectoService.cancelarMiProyecto(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
