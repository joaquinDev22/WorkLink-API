package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EntrevistaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EntrevistaResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IEntrevistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruiter/entrevistas")
@RequiredArgsConstructor
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, habilidades y portfolios profesionales.")
public class EntrevistaController {

    private final IEntrevistaService entrevistaService;

    @Operation(summary = "Listar entrevistas de mi empresa", description = "Obtiene la lista de entrevistas agendadas pertenecientes exclusivamente a la empresa o recruiter autenticado.")
    @GetMapping
    public ResponseEntity<List<EntrevistaResponseDTO>> getEntrevistas(Authentication authentication) {
        return ResponseEntity.ok(entrevistaService.listarMisEntrevistas(authentication.getName()));
    }

    @Operation(summary = "Agendar entrevista", description = "Permite al recruiter o empresa programar una nueva entrevista con un postulante vinculada a una postulación.")
    @PostMapping
    public ResponseEntity<EntrevistaResponseDTO> agendarEntrevista(@Valid @RequestBody EntrevistaRequestDTO dto) {
        return new ResponseEntity<>(entrevistaService.agendarEntrevista(dto), HttpStatus.CREATED);
    }
}
