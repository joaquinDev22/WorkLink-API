package com.example.Proyecto_Final_Progra3.controller.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.responsedto.InscripcionResponseDTO;
import com.example.Proyecto_Final_Progra3.service.bootcamp.InscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inscripciones")
@AllArgsConstructor
@Tag(name = "Bootcamps", description = "Gestión de bootcamps, incluyendo creación, modificación, consulta y eliminación.")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @Operation(summary = "Inscribir empleado a bootcamp", description = "Permite registrar la inscripción de un empleado en un bootcamp específico.")
    @PostMapping("/{bootcampId}/empleados/{employeeId}")
    public ResponseEntity<InscripcionResponseDTO> realizarInscripcion(
            @PathVariable Integer bootcampId, 
            @PathVariable Integer employeeId) {
        return new ResponseEntity<>(inscripcionService.realizarInscripcion(bootcampId, employeeId), HttpStatus.CREATED);
    }

    @Operation(summary = "Inscribirme a bootcamp", description = "Permite al usuario autenticado inscribirse en un bootcamp disponible.")
    @PostMapping("/me/{bootcampId}")
    public ResponseEntity<InscripcionResponseDTO> realizarMiInscripcion(
            org.springframework.security.core.Authentication authentication,
            @PathVariable Integer bootcampId) {
        String username = authentication.getName();
        return new ResponseEntity<>(inscripcionService.realizarMiInscripcion(username, bootcampId), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener mis inscripciones", description = "Obtiene todas las inscripciones realizadas por el usuario autenticado.")
    @GetMapping("/me")
    public ResponseEntity<java.util.List<InscripcionResponseDTO>> obtenerMisInscripciones(
            org.springframework.security.core.Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(inscripcionService.obtenerMisInscripciones(username));
    }

    @Operation(summary = "Cancelar inscripción", description = "Permite cancelar una inscripción previamente realizada a un bootcamp.")
    @PutMapping("/{inscripcionId}/cancelar")
    public ResponseEntity<InscripcionResponseDTO> cancelarInscripcion(@PathVariable Integer inscripcionId) {
        return ResponseEntity.ok(inscripcionService.cancelarInscripcion(inscripcionId));
    }
}
