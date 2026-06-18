package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PagosRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;
import com.example.Proyecto_Final_Progra3.service.freelance.PagosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/freelance/proyectos/{proyectoId}/pagos")
@RequiredArgsConstructor
public class PagosController {

    private final PagosService pagosService;

    @PostMapping
    public ResponseEntity<PagosResponseDTO> registrarPago(
            @PathVariable Integer proyectoId,
            @Valid @RequestBody PagosRequestDTO dto) {
        return ResponseEntity.ok(pagosService.registrarPago(proyectoId, dto));
    }

    @GetMapping
    public ResponseEntity<List<PagosResponseDTO>> listarPagos(@PathVariable Integer proyectoId) {
        return ResponseEntity.ok(pagosService.listarPagos(proyectoId));
    }
}
