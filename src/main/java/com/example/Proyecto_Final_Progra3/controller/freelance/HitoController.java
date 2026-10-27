package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.HitoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.HitoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoHitoProyecto;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IHitoService;
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
@RequestMapping("/api/v1/freelance/proyectos/{proyectoId}/hitos")
@RequiredArgsConstructor
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class HitoController {

    private final IHitoService hitoService;

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto", example = "1")
    @Operation(summary = "Crear hito en proyecto", description = "Permite al cliente propietario agregar un hito de entrega con monto y fecha estimada.")
    @PostMapping
    public ResponseEntity<HitoResponseDTO> crearHito(
            @PathVariable Integer proyectoId,
            @Valid @RequestBody HitoRequestDTO dto,
            Authentication authentication) {
        return new ResponseEntity<>(hitoService.crearHito(proyectoId, dto, authentication.getName()), HttpStatus.CREATED);
    }

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto", example = "1")
    @Operation(summary = "Listar hitos del proyecto", description = "Obtiene la lista cronológica de entregas e hitos asociados al proyecto.")
    @GetMapping
    public ResponseEntity<List<HitoResponseDTO>> listarHitos(@PathVariable Integer proyectoId) {
        return ResponseEntity.ok(hitoService.listarHitosPorProyecto(proyectoId));
    }

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto", example = "1")
    @Parameter(name = "hitoId", description = "Identificador único del hito", example = "1")
    @Parameter(name = "nuevoEstado", description = "Nuevo estado del hito (PENDIENTE, COMPLETADO, PAGADO)", example = "COMPLETADO")
    @Operation(summary = "Cambiar estado de hito", description = "Permite al freelancer marcar entrega (COMPLETADO) o al cliente validar y pagar (PAGADO).")
    @PutMapping("/{hitoId}/estado")
    public ResponseEntity<HitoResponseDTO> cambiarEstado(
            @PathVariable Integer proyectoId,
            @PathVariable Integer hitoId,
            @RequestParam EstadoHitoProyecto nuevoEstado,
            Authentication authentication) {
        return ResponseEntity.ok(hitoService.cambiarEstadoHito(proyectoId, hitoId, nuevoEstado, authentication.getName()));
    }
}

