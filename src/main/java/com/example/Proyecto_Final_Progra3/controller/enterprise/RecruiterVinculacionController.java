package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.responsedto.RecruiterResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IRecruiterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** Endpoints del propio recruiter: ver a qué empresa lo vincularon y pedir la baja. */
@RestController
@RequestMapping("/api/v1/recruiter/vinculacion")
@RequiredArgsConstructor
@Tag(name = "Empleo", description = "Gestión de empresas, vacantes laborales, postulantes, entrevistas, recruiters, habilidades y portfolios profesionales.")
public class RecruiterVinculacionController {

    private final IRecruiterService recruiterService;

    @Operation(summary = "Mi vinculación", description = "Devuelve la empresa a la que está vinculado el recruiter autenticado y el estado de su baja.")
    @GetMapping
    public ResponseEntity<RecruiterResponseDTO> miVinculacion(Authentication authentication) {
        return ResponseEntity.ok(recruiterService.miVinculacion(authentication.getName()));
    }

    @Operation(summary = "Marcar aviso como visto", description = "El recruiter confirma que vio el aviso de que una empresa lo contrató.")
    @PostMapping("/visto")
    public ResponseEntity<RecruiterResponseDTO> marcarVista(Authentication authentication) {
        return ResponseEntity.ok(recruiterService.marcarVinculacionVista(authentication.getName()));
    }

    @Operation(summary = "Solicitar baja", description = "El recruiter pide a la empresa que lo desvincule. Si no responde en 15 días, puede desvincularse por su cuenta.")
    @PostMapping("/baja")
    public ResponseEntity<RecruiterResponseDTO> solicitarBaja(Authentication authentication) {
        return ResponseEntity.ok(recruiterService.solicitarBaja(authentication.getName()));
    }

    @Operation(summary = "Cancelar solicitud de baja", description = "El recruiter retira la solicitud de baja pendiente.")
    @DeleteMapping("/baja")
    public ResponseEntity<RecruiterResponseDTO> cancelarBaja(Authentication authentication) {
        return ResponseEntity.ok(recruiterService.cancelarSolicitudBaja(authentication.getName()));
    }

    @Operation(summary = "Desvincularse por cuenta propia", description = "Disponible cuando pasaron 15 días desde la solicitud de baja sin respuesta de la empresa.")
    @PostMapping("/baja/confirmar")
    public ResponseEntity<Void> confirmarBaja(Authentication authentication) {
        recruiterService.confirmarBajaPorCuentaPropia(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
