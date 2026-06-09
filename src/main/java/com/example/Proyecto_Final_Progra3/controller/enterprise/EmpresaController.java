package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.service.enterprise.EmpresaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/empresa")
public class EmpresaController {
    private final EmpresaService empresaService;

    @PostMapping("/create")
    public EmpresaResponseDTO crearEmpresa(@RequestBody EmpresaRequestDTO dto){
        return empresaService.crearEmpresa(dto);
    }

    @PutMapping("/{id}/edit")
    public EmpresaResponseDTO editarEmpresa(@PathVariable Integer id, @RequestBody EmpresaRequestDTO empresaRequestDTO){
        return empresaService.editarEmpresa(id, empresaRequestDTO);
    }

    @GetMapping("/{id}")
    public Empresa mostrarEmpresa(@PathVariable Integer id){
        return empresaService.buscarEmpresaXid(id);
    }
}
