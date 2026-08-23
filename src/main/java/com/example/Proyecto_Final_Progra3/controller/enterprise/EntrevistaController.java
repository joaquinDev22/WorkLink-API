package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.responsedto.EntrevistaResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IEntrevistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruiter/entrevistas")
@RequiredArgsConstructor
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, habilidades y portfolios profesionales.")
public class EntrevistaController {
    private final IEntrevistaService entrevistaService;
    @Operation(summary = "Listar entrevistas", description = "Obtiene la lista de entrevistas registradas en el sistema junto con información del candidato, vacante, fecha, hora y enlace de acceso.")
    @GetMapping
    public ResponseEntity<List<EntrevistaResponseDTO>> getEntrevistas() {
        // Toda la búsqueda y el mapeo lo hace el servicio
        return ResponseEntity.ok(entrevistaService.listarEntrevistas());
    }
}
