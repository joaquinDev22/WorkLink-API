package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.RecruiterRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.RecruiterResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IRecruiterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empresas/recruiters")
@RequiredArgsConstructor
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, recruiters, habilidades y portfolios profesionales.")
public class RecruiterController {

    private final IRecruiterService recruiterService;

    @Operation(summary = "Crear recruiter", description = "Permite a la empresa autenticada dar de alta un nuevo recruiter asociado a su organización.")
    @PostMapping
    public ResponseEntity<RecruiterResponseDTO> crearRecruiter(
            Authentication authentication,
            @Valid @RequestBody RecruiterRequestDTO dto) {
        return new ResponseEntity<>(recruiterService.crearRecruiter(authentication.getName(), dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar recruiters de la empresa", description = "Obtiene la lista completa de recruiters pertenecientes a la empresa autenticada.")
    @GetMapping
    public ResponseEntity<List<RecruiterResponseDTO>> listarRecruiters(Authentication authentication) {
        return ResponseEntity.ok(recruiterService.listarRecruitersPorEmpresa(authentication.getName()));
    }

    @Parameter(name = "id", description = "Identificador único del recruiter", example = "1")
    @Operation(summary = "Buscar recruiter por ID", description = "Obtiene los detalles de un recruiter asociado a la empresa autenticada.")
    @GetMapping("/{id}")
    public ResponseEntity<RecruiterResponseDTO> buscarPorId(
            Authentication authentication,
            @PathVariable Integer id) {
        return ResponseEntity.ok(recruiterService.buscarPorId(authentication.getName(), id));
    }

    @Parameter(name = "id", description = "Identificador único del recruiter", example = "1")
    @Operation(summary = "Actualizar recruiter", description = "Modifica los datos de un recruiter perteneciente a la empresa autenticada.")
    @PutMapping("/{id}")
    public ResponseEntity<RecruiterResponseDTO> actualizarRecruiter(
            Authentication authentication,
            @PathVariable Integer id,
            @Valid @RequestBody RecruiterRequestDTO dto) {
        return ResponseEntity.ok(recruiterService.actualizarRecruiter(authentication.getName(), id, dto));
    }

    @Parameter(name = "id", description = "Identificador único del recruiter", example = "1")
    @Operation(summary = "Eliminar recruiter", description = "Desvincula y da de baja a un recruiter perteneciente a la empresa autenticada.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecruiter(
            Authentication authentication,
            @PathVariable Integer id) {
        recruiterService.eliminarRecruiter(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}

