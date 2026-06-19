package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.service.enterprise.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, habilidades y portfolios profesionales.")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Parameter(name = "employeeId", description = "Identificador del postulante al que se asociará el currículum", example = "1")
    @Parameter(description = "Archivo del currículum vitae a cargar", required = true)
    @Operation(summary = "Subir CV", description = "Permite cargar y asociar un currículum vitae en formato digital a un postulante específico.")
    @PostMapping("/{employeeId}/cv")
    public ResponseEntity<String> subirCV(@PathVariable Integer employeeId, @RequestParam("file") MultipartFile file) {
        try {
            portfolioService.guardarCV(employeeId, file);
            return ResponseEntity.ok("CV subido exitosamente a la base de datos.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir el CV: " + e.getMessage());
        }
    }
}
