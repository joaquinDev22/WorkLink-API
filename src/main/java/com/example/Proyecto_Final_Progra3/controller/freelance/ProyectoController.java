package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.ProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IProyectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/freelance/proyectos")
@RequiredArgsConstructor
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class ProyectoController {

    private final IProyectoService proyectoService;

    @Operation(summary = "Crear proyecto", description = "Permite registrar un nuevo proyecto freelance en la plataforma.")
    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> crearProyecto(@Valid @RequestBody ProyectoRequestDTO dto) {
        return ResponseEntity.ok(proyectoService.crearProyecto(dto));
    }

    @Operation(summary = "Crear mi proyecto", description = "Permite al cliente autenticado publicar un nuevo proyecto freelance asociado a su cuenta.")
    @PostMapping("/me")
    public ResponseEntity<ProyectoResponseDTO> crearMiProyecto(
            Authentication authentication, 
            @Valid @RequestBody ProyectoRequestDTO dto) {
        return ResponseEntity.ok(proyectoService.crearMiProyecto(authentication.getName(), dto));
    }

    @Parameter(name = "id", description = "Identificador único del proyecto freelance", example = "1")
    @Operation(summary = "Buscar proyecto por ID", description = "Obtiene la información detallada de un proyecto freelance a partir de su identificador numérico.")
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(proyectoService.buscarPorId(id));
    }

    @Parameter(name = "uuid", description = "Identificador seguro UUID v7 del proyecto", example = "0192df80-0000-7000-8000-000000000000")
    @Operation(summary = "Buscar proyecto por UUID v7", description = "Obtiene la información del proyecto protegiendo contra enumeración.")
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<ProyectoResponseDTO> buscarPorUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(proyectoService.buscarPorUuid(uuid));
    }

    @Operation(summary = "Listar proyectos", description = "Obtiene la lista completa de proyectos freelance publicados en la plataforma.")
    @GetMapping
    public ResponseEntity<List<ProyectoResponseDTO>> listarProyectos() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    @Operation(summary = "Listar mis proyectos publicados", description = "Obtiene todos los proyectos publicados por el cliente autenticado.")
    @GetMapping("/me/publicados")
    public ResponseEntity<List<ProyectoResponseDTO>> listarMisProyectosPublicados(Authentication authentication) {
        return ResponseEntity.ok(proyectoService.listarMisProyectosPublicados(authentication.getName()));
    }

    @Operation(summary = "Listar mis proyectos asignados", description = "Obtiene todos los proyectos asignados al freelancer autenticado.")
    @GetMapping("/me/asignados")
    public ResponseEntity<List<ProyectoResponseDTO>> listarMisProyectosAsignados(Authentication authentication) {
        return ResponseEntity.ok(proyectoService.listarMisProyectosAsignados(authentication.getName()));
    }

    @Parameter(name = "id", description = "Identificador único del proyecto freelance", example = "1")
    @Operation(summary = "Cancelar proyecto", description = "Permite al propietario del proyecto cancelar un proyecto freelance publicado.")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarMiProyecto(@PathVariable Integer id, Authentication authentication) {
        proyectoService.cancelarMiProyecto(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @Parameter(name = "id", description = "Identificador único del proyecto freelance", example = "1")
    @Operation(summary = "Modificar proyecto", description = "Permite al cliente propietario modificar los datos de un proyecto freelance en estado BUSCANDO_TALENTO.")
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> actualizarMiProyecto(
            @PathVariable Integer id,
            Authentication authentication,
            @Valid @RequestBody ProyectoRequestDTO dto) {
        return ResponseEntity.ok(proyectoService.actualizarMiProyecto(id, authentication.getName(), dto));
    }

    @Parameter(name = "id", description = "Identificador único del proyecto freelance", example = "1")
    @Parameter(name = "employeeId", description = "Identificador del freelancer contratado", example = "5")
    @Operation(summary = "Asignar freelancer", description = "Permite al cliente propietario adjudicar el proyecto a un freelancer postulado, pasando el estado a EN_PROGRESO.")
    @PutMapping("/{id}/asignar")
    public ResponseEntity<ProyectoResponseDTO> asignarFreelancer(
            @PathVariable Integer id,
            @RequestParam Integer employeeId,
            Authentication authentication) {
        return ResponseEntity.ok(proyectoService.asignarFreelancer(id, employeeId, authentication.getName()));
    }
}
