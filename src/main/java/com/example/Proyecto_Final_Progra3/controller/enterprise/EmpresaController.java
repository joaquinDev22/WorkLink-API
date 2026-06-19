package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.EmpresaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/empresas")
public class EmpresaController {
    private final EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> crearEmpresa(@Valid @RequestBody EmpresaRequestDTO dto) {
        return new ResponseEntity<>(empresaService.crearEmpresa(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> editarEmpresa(@PathVariable Integer id,
            @Valid @RequestBody EmpresaRequestDTO empresaRequestDTO) {
        return ResponseEntity.ok(empresaService.editarEmpresa(id, empresaRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> mostrarEmpresa(@PathVariable Integer id) {
        return ResponseEntity.ok(empresaService.buscarEmpresaXid(id));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<java.util.Map<String, Long>> getDashboardStats(
            org.springframework.security.core.Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(empresaService.getDashboardStats(authentication.getName()));
    }
}
