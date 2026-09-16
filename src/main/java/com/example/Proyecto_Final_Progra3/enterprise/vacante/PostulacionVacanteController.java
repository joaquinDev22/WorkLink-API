package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vacantes/{vacanteId}/postulaciones")
@RequiredArgsConstructor
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, habilidades y portfolios profesionales.")
public class PostulacionVacanteController {

    private final IPostulacionVacanteService postulacionService;

    @Parameter(name = "vacanteId", description = "Identificador de la vacante", example = "1")
    @Parameter(name = "employeeId", description = "Identificador del empleado que realiza la postulación", example = "5")
    @Parameter(name = "mensaje", description = "Mensaje de presentación enviado junto con la postulación", example = "Estoy interesado en la vacante y considero que mi perfil se ajusta a los requisitos.")
    @Parameter(name = "cv", description = "Archivo de currículum vitae adjunto a la postulación")
    @Operation(summary = "Postularse a una vacante", description = "Permite registrar la postulación de un empleado a una vacante adjuntando un mensaje y opcionalmente un currículum vitae.")
    @PostMapping
    public ResponseEntity<PostulacionResponseDTO> postularse(
            @PathVariable Integer vacanteId,
            @RequestParam("employeeId") Integer employeeId,
            @RequestParam("mensaje") String mensaje,
            @RequestParam(value = "cv", required = false) MultipartFile cv) {
        return ResponseEntity.ok(postulacionService.postularse(vacanteId, employeeId, mensaje, cv));
    }

    @Parameter(name = "vacanteId", description = "Identificador de la vacante", example = "1")
    @Parameter(name = "mensaje", description = "Mensaje de presentación enviado junto con la postulación", example = "Estoy interesado en la vacante y considero que mi perfil se ajusta a los requisitos.")
    @Operation(summary = "Postularme a una vacante", description = "Permite al usuario autenticado postularse a una vacante adjuntando un mensaje y opcionalmente un currículum vitae.")
    @PostMapping("/me")
    public ResponseEntity<PostulacionResponseDTO> postularseMe(
            @PathVariable Integer vacanteId,
            org.springframework.security.core.Authentication authentication,
            @RequestParam("mensaje") String mensaje,
            @RequestParam(value = "cv", required = false) MultipartFile cv) {
        return ResponseEntity.ok(postulacionService.postularseMe(authentication.getName(), vacanteId, mensaje, cv));
    }

    @Parameter(name = "vacanteId", description = "Identificador de la vacante", example = "1")
    @Operation(summary = "Ver postulantes de una vacante", description = "Obtiene la lista de postulantes registrados para una vacante específica.")
    @GetMapping
    public ResponseEntity<List<PostulacionResponseDTO>> verPostulantes(@PathVariable Integer vacanteId) {
        return ResponseEntity.ok(postulacionService.listarPostulantes(vacanteId));
    }
}
