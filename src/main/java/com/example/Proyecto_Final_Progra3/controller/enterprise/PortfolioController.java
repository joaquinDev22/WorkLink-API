package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.service.enterprise.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/{employeeId}/cv")
    public ResponseEntity<String> subirCV(@PathVariable Integer employeeId, @RequestParam("file") MultipartFile file) {
        try {
            portfolioService.guardarCV(employeeId, file);
            return ResponseEntity.ok("CV subido exitosamente a la base de datos.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir el CV: " + e.getMessage());
        }
    }
}
