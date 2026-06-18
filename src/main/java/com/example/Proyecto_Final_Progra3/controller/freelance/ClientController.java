package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.service.freelance.ClientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {
    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @org.springframework.web.bind.annotation.GetMapping("/dashboard")
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> getDashboard() {
        return org.springframework.http.ResponseEntity.ok(java.util.Map.of(
                "activeProjects", 2,
                "completedProjects", 5,
                "totalSpent", 4500,
                "pendingProposals", 3));
    }

    @org.springframework.web.bind.annotation.GetMapping("/proyectos")
    public org.springframework.http.ResponseEntity<java.util.List<java.util.Map<String, Object>>> getProyectos() {
        return org.springframework.http.ResponseEntity.ok(java.util.List.of(
                java.util.Map.of("id", 1, "title", "Proyecto Dummy", "status", "Activo", "budget", 1000)));
    }
}
