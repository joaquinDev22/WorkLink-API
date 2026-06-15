package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
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
    public ResponseEntity<EmpresaResponseDTO> crearEmpresa(@Valid @RequestBody EmpresaRequestDTO dto){
        return new ResponseEntity<>(empresaService.crearEmpresa(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> editarEmpresa(@PathVariable Integer id, @Valid @RequestBody EmpresaRequestDTO empresaRequestDTO){
        return ResponseEntity.ok(empresaService.editarEmpresa(id, empresaRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> mostrarEmpresa(@PathVariable Integer id){
        // Since we changed to return DTO, we need to adapt this, but for the scope of the plan, we assume the service does the mapping or we do it here.
        // The service should be updated to return EmpresaResponseDTO in searching.
        // Assuming the service is updated in task 8.
        // Let's call the service and return what it returns, we'll update service next.
        return ResponseEntity.ok(empresaService.buscarEmpresaXid(id));
    }
}
