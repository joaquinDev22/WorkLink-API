package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/empresas")
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, habilidades y portfolios profesionales.")
public class EmpresaController {
    private final EmpresaService empresaService;

    @Operation(summary = "Crear empresa", description = "Crea una nueva empresa en el sistema.")
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> crearEmpresa(@Valid @RequestBody EmpresaRequestDTO dto) {
        return new ResponseEntity<>(empresaService.crearEmpresa(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Editar empresa", description = "Actualiza la información de una empresa existente mediante su identificador.")
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> editarEmpresa(@PathVariable Integer id,
            @Valid @RequestBody EmpresaRequestDTO empresaRequestDTO) {
        return ResponseEntity.ok(empresaService.editarEmpresa(id, empresaRequestDTO));
    }

    @Operation(summary = "Buscar empresa por ID", description = "Obtiene la información detallada de una empresa a partir de su identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> mostrarEmpresa(@PathVariable Integer id) {
        return ResponseEntity.ok(empresaService.buscarEmpresaXid(id));
    }

    @Operation(summary = "Obtener estadísticas del dashboard", description = "Obtiene las estadísticas principales de la empresa autenticada para mostrar en el panel de control.")
    @GetMapping("/dashboard")
    public ResponseEntity<java.util.Map<String, Long>> getDashboardStats(
            org.springframework.security.core.Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(empresaService.getDashboardStats(authentication.getName()));
    }
}
