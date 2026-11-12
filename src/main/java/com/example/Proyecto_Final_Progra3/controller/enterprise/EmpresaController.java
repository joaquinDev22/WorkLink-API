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

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/empresas")
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, habilidades y portfolios profesionales.")
public class EmpresaController {
    private final IEmpresaService empresaService;

    @Operation(summary = "Obtener mi empresa", description = "Obtiene los datos de la empresa vinculada al usuario autenticado.")
    @GetMapping("/my")
    public ResponseEntity<EmpresaResponseDTO> obtenerMiEmpresa(Authentication authentication) {
        return ResponseEntity.ok(empresaService.buscarMiEmpresa(authentication.getName()));
    }

    @Operation(summary = "Crear empresa", description = "Crea la empresa del usuario autenticado. El perfil se toma del token, no del body.")
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> crearEmpresa(Authentication authentication, @Valid @RequestBody EmpresaRequestDTO dto) {
        return new ResponseEntity<>(empresaService.crearEmpresa(authentication.getName(), dto), HttpStatus.CREATED);
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

    @Operation(summary = "Subir logo de mi empresa", description = "Guarda el logo (PNG, JPG o WEBP, hasta 5 MB) de la empresa del usuario autenticado.")
    @PostMapping("/logo")
    public ResponseEntity<Map<String, String>> subirLogo(Authentication authentication, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(Map.of("logoUrl", empresaService.subirLogo(authentication.getName(), file)));
    }

    @Operation(summary = "Subir banner de mi empresa", description = "Guarda el banner (PNG, JPG o WEBP, hasta 8 MB) de la empresa del usuario autenticado.")
    @PostMapping("/banner")
    public ResponseEntity<Map<String, String>> subirBanner(Authentication authentication, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(Map.of("bannerUrl", empresaService.subirBanner(authentication.getName(), file)));
    }
}
