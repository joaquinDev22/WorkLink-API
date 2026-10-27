package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.CalificacionProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.CalificacionProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ReputacionPerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.ICalificacionProyectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, calificaciones, reputación y postulaciones.")
public class CalificacionProyectoController {

    private final ICalificacionProyectoService calificacionService;

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto", example = "1")
    @Operation(summary = "Calificar proyecto freelance", description = "Permite al cliente o freelancer participante calificar el desempeño mutuo y registrar la reseña.")
    @PostMapping("/api/v1/freelance/proyectos/{proyectoId}/calificar")
    public ResponseEntity<CalificacionProyectoResponseDTO> calificarProyecto(
            @PathVariable Integer proyectoId,
            @Valid @RequestBody CalificacionProyectoRequestDTO dto,
            Authentication authentication) {
        return new ResponseEntity<>(calificacionService.calificarProyecto(proyectoId, dto, authentication.getName()), HttpStatus.CREATED);
    }

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto", example = "1")
    @Operation(summary = "Listar calificaciones de un proyecto", description = "Obtiene las opiniones y puntajes asociados a un proyecto freelance.")
    @GetMapping("/api/v1/freelance/proyectos/{proyectoId}/calificaciones")
    public ResponseEntity<List<CalificacionProyectoResponseDTO>> listarCalificaciones(@PathVariable Integer proyectoId) {
        return ResponseEntity.ok(calificacionService.listarCalificacionesPorProyecto(proyectoId));
    }

    @Parameter(name = "perfilId", description = "Identificador del perfil de usuario", example = "1")
    @Operation(summary = "Obtener reputación y reseñas de perfil", description = "Calcula el promedio decimal y retorna el listado histórico de reseñas del usuario.")
    @GetMapping("/api/v1/perfiles/{perfilId}/calificaciones")
    public ResponseEntity<ReputacionPerfilResponseDTO> obtenerReputacion(@PathVariable Integer perfilId) {
        return ResponseEntity.ok(calificacionService.obtenerReputacionPerfil(perfilId));
    }
}

