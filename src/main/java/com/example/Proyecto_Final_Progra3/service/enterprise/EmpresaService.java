package com.example.Proyecto_Final_Progra3.service.enterprise;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.example.Proyecto_Final_Progra3.mappers.EmpresaMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class EmpresaService {
    private final EmpresaRepository empresaRepository;
    private final PerfilRepository perfilRepository;
    private final EmpresaMapper mapper;

    @Transactional
    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO dto) {
        Perfil perfil = perfilRepository.findById(dto.getPrefil_id())
                .orElseThrow(() -> new RuntimeException("Ese perfil no existe"));

        Empresa empresa = mapper.toEntity(dto);
        empresa.setPerfil(perfil);

        empresa = empresaRepository.save(empresa);

        return mapper.toDto(empresa);
    }

    @Transactional
    public EmpresaResponseDTO editarEmpresa(Integer id, EmpresaRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Esa empresa no existe"));

        mapper.updateEntityFromDto(dto, empresa);

        empresa = empresaRepository.save(empresa);

        return mapper.toDto(empresa);
    }

    public List<EmpresaResponseDTO> mostrarEmpresas() {
        return empresaRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public EmpresaResponseDTO buscarEmpresaXid(Integer id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ese empresa no existe"));
        return mapper.toDto(empresa);
    }

    public List<EmpresaResponseDTO> buscarEmpresasXidPerfil(int idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RuntimeException("Ese perfil no existe"));
        return empresaRepository.findByPerfil(perfil).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarEmpresa(Integer idEmpresa) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Ese empresa no existe"));

        empresaRepository.delete(empresa);
    }
}
