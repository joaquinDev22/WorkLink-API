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
@RequestMapping("/api/v1/vacantes")
@RequiredArgsConstructor
public class VacanteController {

    private final VacanteService vacanteService;

    @PostMapping
    public ResponseEntity<VacanteResponseDTO> crearVacante(@Valid @RequestBody VacanteRequestDTO dto) {
        return ResponseEntity.ok(vacanteService.crearVacante(dto));
    }

    @PostMapping("/me")
    public ResponseEntity<VacanteResponseDTO> crearMiVacante(
            org.springframework.security.core.Authentication authentication, 
            @Valid @RequestBody VacanteRequestDTO dto) {
        return ResponseEntity.ok(vacanteService.crearMiVacante(authentication.getName(), dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacanteResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(vacanteService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<VacanteResponseDTO>> listarVacantes() {
        return ResponseEntity.ok(vacanteService.listarTodas());
    }

    @GetMapping("/me")
    public ResponseEntity<List<VacanteResponseDTO>> listarMisVacantes(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(vacanteService.listarMisVacantes(authentication.getName()));
    }
}
