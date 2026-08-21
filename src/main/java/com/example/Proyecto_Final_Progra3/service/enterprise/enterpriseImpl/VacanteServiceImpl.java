package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.VacanteRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.VacanteResponseDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.model.enterprise.Vacante;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacanteServiceImpl implements IVacanteService {

    private final VacanteRepository vacanteRepository;
    private final EmpresaRepository empresaRepository;
    private final PerfilRepository perfilRepository;
    private final RecruiterRepository recruiterRepository;

    @Override
    @Transactional
    public VacanteResponseDTO crearVacante(VacanteRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.empresaId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        Vacante vacante = new Vacante();
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

        Vacante guardada = vacanteRepository.save(vacante);
        return mapToDto(guardada);
    }

    @Override
    @Transactional
    public VacanteResponseDTO crearMiVacante(String username, VacanteRequestDTO dto) {
        com.example.Proyecto_Final_Progra3.model.Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        Empresa empresa = empresaRepository.findByPerfil(perfil).orElse(null);
        if (empresa == null) {
            com.example.Proyecto_Final_Progra3.model.enterprise.Recruiter recruiter = recruiterRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo las empresas o recruiters pueden crear vacantes"));
            empresa = recruiter.getEmpresa();
            if (empresa == null) {
                throw new RuntimeException("El recruiter no está asociado a ninguna empresa.");
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
    @Transactional(readOnly = true)
    public VacanteResponseDTO buscarPorId(Integer id) {
        return vacanteRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacanteResponseDTO> listarTodas() {
        return vacanteRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacanteResponseDTO> listarMisVacantes(String username) {
        com.example.Proyecto_Final_Progra3.model.Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        Empresa empresa = empresaRepository.findByPerfil(perfil).orElse(null);
        if (empresa == null) {
            com.example.Proyecto_Final_Progra3.model.enterprise.Recruiter recruiter = recruiterRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo las empresas o recruiters tienen vacantes propias"));
            empresa = recruiter.getEmpresa();
        }
        
        return vacanteRepository.findByEmpresa_Id(empresa.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private VacanteResponseDTO mapToDto(Vacante vacante) {
        return new VacanteResponseDTO(
                vacante.getVacanteId(),
                vacante.getPuesto(),
                vacante.getDescripcion(),
                vacante.getPublicacion(),
                vacante.getFinalizacion(),
                vacante.getEmpresa().getId(),
                vacante.getEmpresa().getNombre(),
                vacante.getLogo(),
                vacante.getUbicacion(),
                vacante.getTipoUbicacion(),
                vacante.getTipoVacante(),
                vacante.getSalario(),
                vacante.getEtiquetas() != null ? new java.util.ArrayList<>(vacante.getEtiquetas()) : new java.util.ArrayList<>()
        );
    }
}
