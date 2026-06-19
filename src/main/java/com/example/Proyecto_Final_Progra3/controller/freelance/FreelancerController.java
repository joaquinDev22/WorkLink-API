package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.service.freelance.FreelancerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/freelancer")
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class FreelancerController {
    private final FreelancerService service;
    public FreelancerController(FreelancerService service) {
        this.service = service;
    }

    @Operation(summary = "Consultar finanzas", description = "Obtiene un resumen financiero del freelancer, incluyendo saldo disponible, pagos pendientes y monto total retirado.")
    @GetMapping("/finances")
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> getFinances() {
        return org.springframework.http.ResponseEntity.ok(java.util.Map.of(
            "balance", 1250.00,
            "pending", 450.00,
            "withdrawn", 3000.00
        ));
    }

    @Operation(summary = "Consultar transacciones", description = "Obtiene el historial de transacciones realizadas por el freelancer, incluyendo fecha, monto, estado y proyecto asociado.")
    @GetMapping("/transactions")
    public org.springframework.http.ResponseEntity<java.util.List<java.util.Map<String, Object>>> getTransactions() {
        return org.springframework.http.ResponseEntity.ok(java.util.List.of(
            java.util.Map.of("id", "TX123", "date", "2023-10-01", "amount", 500, "status", "Completed", "project", "App Mobile")
        ));
    }
}
