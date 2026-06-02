package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.DTO.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.DTO.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.service.enterprise.EmpresaService;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/empresa")
public class EmpresaController {
    private final EmpresaService empresaService;

    @PostMapping("/create")
    public EmpresaResponseDTO crearEmpresa(@RequestBody EmpresaRequestDTO empresaRequestDTO){
        return empresaService.crearEmpresa(empresaRequestDTO);
    }

    @PutMapping("/{id}/edit")
    public EmpresaResponseDTO editarEmpresa(@PathVariable int id, @RequestBody EmpresaRequestDTO empresaRequestDTO){
        return empresaService.editarEmpresa(id, empresaRequestDTO);
    }

    @GetMapping("/{id}")
    public Empresa mostrarEmpresa(@PathVariable int id){
        return empresaService.buscarEmpresaXid(id);
    }
}
