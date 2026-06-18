package com.example.Proyecto_Final_Progra3.service.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.VacanteRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.VacanteResponseDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.model.enterprise.Vacante;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.VacanteRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
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
    private final PerfilRepository perfilRepository;

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
        
        vacante.setLogo(dto.getLogo());
        vacante.setUbicacion(dto.getUbicacion());
        vacante.setTipoUbicacion(dto.getTipoUbicacion());
        vacante.setTipoVacante(dto.getTipoVacante());
        vacante.setSalario(dto.getSalario());
        vacante.setEtiquetas(dto.getEtiquetas());

        Vacante guardada = vacanteRepository.save(vacante);
        return mapToDto(guardada);
    }

    @Transactional
    public VacanteResponseDTO crearMiVacante(String username, VacanteRequestDTO dto) {
        com.example.Proyecto_Final_Progra3.model.Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        Empresa empresa = empresaRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo las empresas pueden crear vacantes"));
                
        dto.setEmpresaId(empresa.getId());
        return crearVacante(dto);
    }

    @Transactional(readOnly = true)
    public VacanteResponseDTO buscarPorId(Integer id) {
        return vacanteRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));
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
        
        dto.setLogo(vacante.getLogo());
        dto.setUbicacion(vacante.getUbicacion());
        dto.setTipoUbicacion(vacante.getTipoUbicacion());
        dto.setTipoVacante(vacante.getTipoVacante());
        dto.setSalario(vacante.getSalario());
        dto.setEtiquetas(vacante.getEtiquetas() != null ? new java.util.ArrayList<>(vacante.getEtiquetas()) : new java.util.ArrayList<>());

        return dto;
    }
}
