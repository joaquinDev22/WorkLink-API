package com.example.Proyecto_Final_Progra3.controller.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.responsedto.InscripcionResponseDTO;
import com.example.Proyecto_Final_Progra3.service.bootcamp.InscripcionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inscripciones")
@AllArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping("/{bootcampId}/empleados/{employeeId}")
    public ResponseEntity<InscripcionResponseDTO> realizarInscripcion(
            @PathVariable Integer bootcampId, 
            @PathVariable Integer employeeId) {
        return new ResponseEntity<>(inscripcionService.realizarInscripcion(bootcampId, employeeId), HttpStatus.CREATED);
    }

    @PostMapping("/me/{bootcampId}")
    public ResponseEntity<InscripcionResponseDTO> realizarMiInscripcion(
            org.springframework.security.core.Authentication authentication,
            @PathVariable Integer bootcampId) {
        String username = authentication.getName();
        return new ResponseEntity<>(inscripcionService.realizarMiInscripcion(username, bootcampId), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<java.util.List<InscripcionResponseDTO>> obtenerMisInscripciones(
            org.springframework.security.core.Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(inscripcionService.obtenerMisInscripciones(username));
    }

    @PutMapping("/{inscripcionId}/cancelar")
    public ResponseEntity<InscripcionResponseDTO> cancelarInscripcion(@PathVariable Integer inscripcionId) {
        return ResponseEntity.ok(inscripcionService.cancelarInscripcion(inscripcionId));
    }
}
