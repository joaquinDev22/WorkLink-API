package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PagosRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;
import com.example.Proyecto_Final_Progra3.service.freelance.PagosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/freelance/proyectos/{proyectoId}/pagos")
@RequiredArgsConstructor
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class PagosController {

    private final PagosService pagosService;

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto", example = "1")
    @Operation(summary = "Registrar pago", description = "Permite registrar un nuevo pago asociado a un proyecto específico.")
    @PostMapping
    public ResponseEntity<PagosResponseDTO> registrarPago(
            @PathVariable Integer proyectoId,
            @Valid @RequestBody PagosRequestDTO dto) {
        return ResponseEntity.ok(pagosService.registrarPago(proyectoId, dto));
    }

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto", example = "1")
    @Operation(summary = "Listar pagos de un proyecto", description = "Obtiene el historial completo de pagos registrados para un proyecto determinado.")
    @GetMapping
    public ResponseEntity<List<PagosResponseDTO>> listarPagos(@PathVariable Integer proyectoId) {
        return ResponseEntity.ok(pagosService.listarPagos(proyectoId));
    }
}
