package com.example.Proyecto_Final_Progra3.service.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.VacanteRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.VacanteResponseDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.model.enterprise.Vacante;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.VacanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacanteService {

    private final VacanteRepository vacanteRepository;
    private final EmpresaRepository empresaRepository;

    @Transactional
    public VacanteResponseDTO crearVacante(VacanteRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        Vacante vacante = new Vacante();
        vacante.setPuesto(dto.getPuesto());
        vacante.setDescripcion(dto.getDescripcion());
        vacante.setPublicacion(LocalDate.now());
        vacante.setFinalizacion(dto.getFinalizacion());
        vacante.setEmpresa(empresa);

        Vacante guardada = vacanteRepository.save(vacante);
        return mapToDto(guardada);
    }

    @Transactional(readOnly = true)
    public List<VacanteResponseDTO> listarTodas() {
        return vacanteRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private VacanteResponseDTO mapToDto(Vacante vacante) {
        VacanteResponseDTO dto = new VacanteResponseDTO();
        dto.setVacanteId(vacante.getVacanteId());
        dto.setPuesto(vacante.getPuesto());
        dto.setDescripcion(vacante.getDescripcion());
        dto.setPublicacion(vacante.getPublicacion());
        dto.setFinalizacion(vacante.getFinalizacion());
        dto.setEmpresaId(vacante.getEmpresa().getId());
        dto.setNombreEmpresa(vacante.getEmpresa().getNombre());
        return dto;
    }
}
