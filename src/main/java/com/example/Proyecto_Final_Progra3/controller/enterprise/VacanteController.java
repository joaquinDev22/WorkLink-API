package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.VacanteRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.VacanteResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IVacanteService;
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
            Authentication authentication, 
            @Valid @RequestBody VacanteRequestDTO dto) {
        return ResponseEntity.ok(vacanteService.crearMiVacante(authentication.getName(), dto));
    }

    @Parameter(name = "id", description = "Identificador único de la vacante", example = "1")
    @Operation(summary = "Buscar vacante por ID", description = "Obtiene la información detallada de una vacante a partir de su identificador numérico.")
    @GetMapping("/{id}")
    public ResponseEntity<VacanteResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(vacanteService.buscarPorId(id));
    }

    @Parameter(name = "uuid", description = "Identificador seguro UUID v7 de la vacante", example = "0192df80-0000-7000-8000-000000000000")
    @Operation(summary = "Buscar vacante por UUID v7", description = "Obtiene la información pública de la vacante protegiendo contra enumeración.")
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<VacanteResponseDTO> buscarPorUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(vacanteService.buscarPorUuid(uuid));
    }

    @Operation(summary = "Listar vacantes", description = "Obtiene la lista completa de vacantes publicadas en la plataforma.")
    @GetMapping
    public ResponseEntity<List<VacanteResponseDTO>> listarVacantes() {
        return ResponseEntity.ok(vacanteService.listarTodas());
    }

    @Operation(summary = "Listar mis vacantes", description = "Obtiene todas las vacantes publicadas por la empresa autenticada.")
    @GetMapping("/me")
    public ResponseEntity<List<VacanteResponseDTO>> listarMisVacantes(Authentication authentication) {
        return ResponseEntity.ok(vacanteService.listarMisVacantes(authentication.getName()));
    }

    @Parameter(name = "id", description = "Identificador único de la vacante", example = "1")
    @Operation(summary = "Modificar vacante", description = "Permite a la empresa o recruiter autenticado actualizar la información de una vacante de su propiedad.")
    @PutMapping("/{id}")
    public ResponseEntity<VacanteResponseDTO> actualizarVacante(
            @PathVariable Integer id,
            Authentication authentication,
            @Valid @RequestBody VacanteRequestDTO dto) {
        return ResponseEntity.ok(vacanteService.actualizarVacante(id, authentication.getName(), dto));
    }

    @Parameter(name = "id", description = "Identificador único de la vacante", example = "1")
    @Operation(summary = "Eliminar vacante", description = "Permite a la empresa o recruiter dar de baja y eliminar una vacante laboral de su propiedad.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVacante(
            @PathVariable Integer id,
            Authentication authentication) {
        vacanteService.eliminarVacante(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
