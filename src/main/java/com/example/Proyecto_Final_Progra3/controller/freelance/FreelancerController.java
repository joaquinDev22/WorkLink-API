package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.service.freelance.FreelancerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/freelancer")
public class FreelancerController {
    private final FreelancerService service;
    public FreelancerController(FreelancerService service) {
        this.service = service;
    }

    @org.springframework.web.bind.annotation.GetMapping("/finances")
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> getFinances() {
        return org.springframework.http.ResponseEntity.ok(java.util.Map.of(
            "balance", 1250.00,
            "pending", 450.00,
            "withdrawn", 3000.00
        ));
    }

    @org.springframework.web.bind.annotation.GetMapping("/transactions")
    public org.springframework.http.ResponseEntity<java.util.List<java.util.Map<String, Object>>> getTransactions() {
        return org.springframework.http.ResponseEntity.ok(java.util.List.of(
            java.util.Map.of("id", "TX123", "date", "2023-10-01", "amount", 500, "status", "Completed", "project", "App Mobile")
        ));
    }
}
