package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import com.example.Proyecto_Final_Progra3.errors.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.errors.UnauthorizedAccessException;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;
import com.example.Proyecto_Final_Progra3.enterprise.empresa.EmpresaEntity;
import com.example.Proyecto_Final_Progra3.enterprise.empresa.RecruiterEntity;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilRepository;
import com.example.Proyecto_Final_Progra3.enterprise.empresa.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.enterprise.empresa.RecruiterRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacanteServiceImpl implements IVacanteService {

    private final VacanteRepository vacanteRepository;
    private final EmpresaRepository empresaRepository;
    private final PerfilRepository perfilRepository;
    private final RecruiterRepository recruiterRepository;
    private final VacanteMapper vacanteMapper;

    @Override
    @Transactional
    public VacanteResponseDTO crearVacante(VacanteRequestDTO dto) {
        EmpresaEntity empresa = empresaRepository.findById(dto.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + dto.empresaId()));

        VacanteEntity vacante = new VacanteEntity();
        vacante.setPuesto(dto.puesto());
        vacante.setDescripcion(dto.descripcion());
        vacante.setPublicacion(LocalDate.now());
        vacante.setFinalizacion(dto.finalizacion());
        vacante.setEmpresa(empresa);
        
        vacante.setLogo(dto.logo());
        vacante.setUbicacion(dto.ubicacion());
        vacante.setTipoUbicacion(dto.tipoUbicacion());
        vacante.setTipoVacante(dto.tipoVacante());
        vacante.setSalario(dto.salario());
        vacante.setEtiquetas(dto.etiquetas());

        VacanteEntity guardada = vacanteRepository.save(vacante);
        return vacanteMapper.toDto(guardada);
    }

    @Override
    @Transactional
    public VacanteResponseDTO crearMiVacante(String username, VacanteRequestDTO dto) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        EmpresaEntity empresa = empresaRepository.findByPerfil(perfil).orElse(null);
        if (empresa == null) {
            RecruiterEntity recruiter = recruiterRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("Solo las empresas o recruiters pueden crear vacantes"));
            empresa = recruiter.getEmpresa();
            if (empresa == null) {
                throw new BusinessRuleException("El recruiter no está asociado a ninguna empresa.");
            }
        }
                
        VacanteRequestDTO dtoConEmpresa = new VacanteRequestDTO(
                dto.puesto(),
                dto.descripcion(),
                dto.finalizacion(),
                empresa.getId(),
                dto.logo(),
                dto.ubicacion(),
                dto.tipoUbicacion(),
                dto.tipoVacante(),
                dto.salario(),
                dto.etiquetas()
        );
        return crearVacante(dtoConEmpresa);
    }

    @Override
    public VacanteResponseDTO buscarPorId(Integer id) {
        return vacanteRepository.findById(id)
                .map(vacanteMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + id));
    }

    @Override
    public List<VacanteResponseDTO> listarTodas() {
        return vacanteMapper.toDtoList(vacanteRepository.findAll());
    }

    @Override
    public List<VacanteResponseDTO> listarMisVacantes(String username) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        EmpresaEntity empresa = empresaRepository.findByPerfil(perfil).orElse(null);
        if (empresa == null) {
            RecruiterEntity recruiter = recruiterRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("Solo las empresas o recruiters tienen vacantes propias"));
            empresa = recruiter.getEmpresa();
        }
        
        return vacanteMapper.toDtoList(vacanteRepository.findByEmpresa_Id(empresa.getId()));
    }
}
