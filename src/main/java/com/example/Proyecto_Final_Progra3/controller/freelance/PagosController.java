package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.responsedto.MercadoPagoPreferenceResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IPagosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/freelance/proyectos/{proyectoId}/pagos")
@RequiredArgsConstructor
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class PagosController {

    private final IPagosService pagosService;

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto", example = "1")
    @Operation(summary = "Listar pagos de un proyecto", description = "Historial de pagos del proyecto; solo lo ven el cliente y el freelancer asignado.")
    @GetMapping
    public ResponseEntity<List<PagosResponseDTO>> listarPagos(@PathVariable Integer proyectoId, Authentication authentication) {
        return ResponseEntity.ok(pagosService.listarPagos(proyectoId, authentication.getName()));
    }

    @Parameter(name = "proyectoId", description = "Identificador único del proyecto", example = "1")
    @Parameter(name = "hitoId", description = "Hito a pagar: debe ser el próximo pendiente del proyecto", example = "3")
    @Operation(summary = "Crear preferencia Checkout Pro para un hito", description = "El cliente titular paga por adelantado el próximo hito pendiente; el monto lo define el hito.")
    @PostMapping("/checkout")
    public ResponseEntity<MercadoPagoPreferenceResponseDTO> crearCheckout(
            @PathVariable Integer proyectoId,
            @RequestParam Integer hitoId,
            Authentication authentication) {
        return ResponseEntity.ok(pagosService.crearPreferenciaPago(proyectoId, hitoId, authentication.getName()));
    }
}
