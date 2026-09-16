package com.example.Proyecto_Final_Progra3.enterprise.talento;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habilidades")
@RequiredArgsConstructor
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, habilidades y portfolios profesionales.")
public class HabilidadController {
    private final IEmployeeService service;

    @Operation(summary = "Listar habilidades", description = "Obtiene la lista completa de habilidades registradas en la plataforma.")
    @GetMapping
    public ResponseEntity<List<HabilidadResponseDTO>> getAllHabilidades() {
        List<HabilidadResponseDTO> response = service.getAllHabilities().stream()
                .map(h -> new HabilidadResponseDTO(h.getHabilidadId(), h.getNombreHabilidad()))
                .toList();
        return ResponseEntity.ok(response);
    }
}
