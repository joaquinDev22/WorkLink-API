package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vacantes")
@RequiredArgsConstructor
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, habilidades y portfolios profesionales.")
public class VacanteController {

    private final IVacanteService vacanteService;

    @Operation(summary = "Crear vacante", description = "Permite registrar una nueva oferta laboral en la plataforma.")
    @PostMapping
    public ResponseEntity<VacanteResponseDTO> crearVacante(@Valid @RequestBody VacanteRequestDTO dto) {
        return ResponseEntity.ok(vacanteService.crearVacante(dto));
    }

    @Operation(summary = "Crear mi vacante", description = "Permite a la empresa autenticada crear una nueva oferta laboral asociada a su cuenta.")
    @PostMapping("/me")
    public ResponseEntity<VacanteResponseDTO> crearMiVacante(
            org.springframework.security.core.Authentication authentication, 
            @Valid @RequestBody VacanteRequestDTO dto) {
        return ResponseEntity.ok(vacanteService.crearMiVacante(authentication.getName(), dto));
    }

    @Parameter(name = "id", description = "Identificador único de la vacante", example = "1")
    @Operation(summary = "Buscar vacante por ID", description = "Obtiene la información detallada de una vacante a partir de su identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<VacanteResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(vacanteService.buscarPorId(id));
    }

    @Operation(summary = "Listar vacantes", description = "Obtiene la lista completa de vacantes publicadas en la plataforma.")
    @GetMapping
    public ResponseEntity<List<VacanteResponseDTO>> listarVacantes() {
        return ResponseEntity.ok(vacanteService.listarTodas());
    }

    @Operation(summary = "Listar mis vacantes", description = "Obtiene todas las vacantes publicadas por la empresa autenticada.")
    @GetMapping("/me")
    public ResponseEntity<List<VacanteResponseDTO>> listarMisVacantes(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(vacanteService.listarMisVacantes(authentication.getName()));
    }
}
