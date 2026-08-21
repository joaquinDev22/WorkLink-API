package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PostulacionProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IPostulacionProyectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/freelance/proyectos/{proyectoId}/postulaciones")
@RequiredArgsConstructor
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class PostulacionProyectoController {

    private final IPostulacionProyectoService postulacionService;

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto freelance", example = "1")
    @Operation(summary = "Enviar propuesta a proyecto", description = "Permite enviar una propuesta para participar en un proyecto freelance específico.")
    @PostMapping
    public ResponseEntity<PostulacionProyectoResponseDTO> enviarPropuesta(
            @PathVariable Integer proyectoId,
            @Valid @RequestBody PostulacionProyectoRequestDTO dto) {
        return ResponseEntity.ok(postulacionService.postularse(proyectoId, dto));
    }

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto freelance", example = "1")
    @Operation(summary = "Enviar mi propuesta", description = "Permite al freelancer autenticado enviar una propuesta para participar en un proyecto freelance.")
    @PostMapping("/me")
    public ResponseEntity<PostulacionProyectoResponseDTO> enviarMiPropuesta(
            @PathVariable Integer proyectoId,
            org.springframework.security.core.Authentication authentication,
            @Valid @RequestBody PostulacionProyectoRequestDTO dto) {
        return ResponseEntity.ok(postulacionService.postularseMe(authentication.getName(), proyectoId, dto));
    }

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto freelance", example = "1")
    @Operation(summary = "Listar propuestas de un proyecto", description = "Obtiene todas las propuestas enviadas por freelancers para un proyecto específico.")
    @GetMapping
    public ResponseEntity<List<PostulacionProyectoResponseDTO>> listarPropuestas(@PathVariable Integer proyectoId) {
        return ResponseEntity.ok(postulacionService.listarPostulacionesPorProyecto(proyectoId));
    }
}
