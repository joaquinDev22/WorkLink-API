package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.VacanteRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.VacanteResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.mappers.VacanteMapper;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.EmpresaEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.RecruiterEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.VacanteEntity;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.RecruiterRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.VacanteRepository;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IVacanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
    public VacanteResponseDTO buscarPorUuid(UUID uuid) {
        return vacanteRepository.findByUuid(uuid)
                .map(vacanteMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con UUID: " + uuid));
    }

    @Override
    public List<VacanteResponseDTO> listarTodas() {
        return vacanteMapper.toDtoList(vacanteRepository.findByActivaTrue());
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

    private void validarPropiedadVacante(VacanteEntity vacante, String username) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        EmpresaEntity empresa = empresaRepository.findByPerfil(perfil).orElse(null);
        if (empresa == null) {
            RecruiterEntity recruiter = recruiterRepository.findByPerfil(perfil)
                    .orElseThrow(() -> new UnauthorizedAccessException("Solo las empresas o recruiters asociados pueden modificar o eliminar vacantes"));
            empresa = recruiter.getEmpresa();
        }

        if (empresa == null || !vacante.getEmpresa().getId().equals(empresa.getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para modificar o eliminar esta vacante");
        }
    }

    @Override
    @Transactional
    public VacanteResponseDTO actualizarVacante(Integer id, String username, VacanteRequestDTO dto) {
        VacanteEntity vacante = vacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + id));

        validarPropiedadVacante(vacante, username);

        vacante.setPuesto(dto.puesto());
        vacante.setDescripcion(dto.descripcion());
        vacante.setFinalizacion(dto.finalizacion());
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
    public void eliminarVacante(Integer id, String username) {
        VacanteEntity vacante = vacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + id));

        validarPropiedadVacante(vacante, username);
        vacante.setActiva(false);
        vacanteRepository.save(vacante);
    }
}
