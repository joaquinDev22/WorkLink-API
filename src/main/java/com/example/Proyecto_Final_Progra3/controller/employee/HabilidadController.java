package com.example.Proyecto_Final_Progra3.controller.employee;

import com.example.Proyecto_Final_Progra3.model.employee.Habilidad;
import com.example.Proyecto_Final_Progra3.repository.employee.HabilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habilidades")
@RequiredArgsConstructor
public class HabilidadController {

    private final HabilidadRepository habilidadRepository;

    @GetMapping
    public ResponseEntity<List<Habilidad>> getAllHabilidades() {
        return ResponseEntity.ok(habilidadRepository.findAll());
    }
}
