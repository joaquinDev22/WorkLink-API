package com.example.Proyecto_Final_Progra3.freelance.freelancer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/freelancer")
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class FreelancerController {

    @Operation(summary = "Consultar finanzas", description = "Obtiene un resumen financiero del freelancer, incluyendo saldo disponible, pagos pendientes y monto total retirado.")
    @GetMapping("/finances")
    public ResponseEntity<Map<String, Object>> getFinances() {
        return ResponseEntity.ok(Map.of(
            "balance", 1250.00,
            "pending", 450.00,
            "withdrawn", 3000.00
        ));
    }

    @Operation(summary = "Consultar transacciones", description = "Obtiene el historial de transacciones realizadas por el freelancer, incluyendo fecha, monto, estado y proyecto asociado.")
    @GetMapping("/transactions")
    public ResponseEntity<List<Map<String, Object>>> getTransactions() {
        return ResponseEntity.ok(List.of(
            Map.of("id", "TX123", "date", "2023-10-01", "amount", 500, "status", "Completed", "project", "App Mobile")
        ));
    }
}
