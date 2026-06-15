package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.VacanteRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.VacanteResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.VacanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacantes")
@RequiredArgsConstructor
public class VacanteController {

    private final VacanteService vacanteService;

    @PostMapping
    public ResponseEntity<VacanteResponseDTO> crearVacante(@Valid @RequestBody VacanteRequestDTO dto) {
        return ResponseEntity.ok(vacanteService.crearVacante(dto));
    }

    @GetMapping
    public ResponseEntity<List<VacanteResponseDTO>> listarVacantes() {
        return ResponseEntity.ok(vacanteService.listarTodas());
    }
}
