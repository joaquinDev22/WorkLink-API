package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.DashboardStatsResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IEmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/empresas")
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, habilidades y portfolios profesionales.")
public class EmpresaController {
    private final IEmpresaService empresaService;

    @Operation(summary = "Crear empresa", description = "Crea una nueva empresa en el sistema.")
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> crearEmpresa(@Valid @RequestBody EmpresaRequestDTO dto) {
        return new ResponseEntity<>(empresaService.crearEmpresa(dto), HttpStatus.CREATED);
    }

    @Parameter(name = "id", description = "Identificador único de la empresa", example = "1")
    @Operation(summary = "Editar empresa", description = "Actualiza la información de una empresa existente validando pertenencia del usuario autenticado.")
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> editarEmpresa(
            @PathVariable Integer id,
            @Valid @RequestBody EmpresaRequestDTO empresaRequestDTO,
            Authentication authentication) {
        return ResponseEntity.ok(empresaService.editarEmpresa(id, authentication.getName(), empresaRequestDTO));
    }

    @Parameter(name = "id", description = "Identificador único de la empresa", example = "1")
    @Operation(summary = "Buscar empresa por ID", description = "Obtiene la información detallada de una empresa a partir de su identificador numérico.")
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> mostrarEmpresa(@PathVariable Integer id) {
        return ResponseEntity.ok(empresaService.buscarEmpresaXid(id));
    }

    @Parameter(name = "uuid", description = "Identificador seguro UUID v7 de la empresa", example = "0192df80-0000-7000-8000-000000000000")
    @Operation(summary = "Buscar empresa por UUID v7", description = "Obtiene la información detallada de una empresa protegiendo contra enumeración.")
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<EmpresaResponseDTO> mostrarEmpresaPorUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(empresaService.buscarEmpresaXuuid(uuid));
    }

    @Operation(summary = "Obtener estadísticas del dashboard", description = "Obtiene las estadísticas principales de la empresa autenticada para mostrar en el panel de control.")
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponseDTO> getDashboardStats(Authentication authentication){
        return ResponseEntity.ok(empresaService.getDashboardStats(authentication.getName()));
    }
}
