package com.example.Proyecto_Final_Progra3.controller.freelance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client")
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class ClientController {

    @Operation(summary = "Obtener dashboard del cliente", description = "Obtiene las estadísticas principales del cliente, incluyendo proyectos activos, proyectos completados, presupuesto invertido y propuestas pendientes.")
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(Map.of(
                "activeProjects", 2,
                "completedProjects", 5,
                "totalSpent", 4500,
                "pendingProposals", 3));
    }

    @Operation(summary = "Listar proyectos del cliente", description = "Obtiene la lista de proyectos asociados al cliente junto con su estado y presupuesto.")
    @GetMapping("/proyectos")
    public ResponseEntity<List<Map<String, Object>>> getProyectos() {
        return ResponseEntity.ok(List.of(
                Map.of("id", 1, "title", "Proyecto Dummy", "status", "Activo", "budget", 1000)));
    }
}
