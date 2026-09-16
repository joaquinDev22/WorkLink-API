package com.example.Proyecto_Final_Progra3.enterprise.empresa;

import com.example.Proyecto_Final_Progra3.enterprise.vacante.VacanteRepository;
import com.example.Proyecto_Final_Progra3.enterprise.vacante.PostulacionRepository;
import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class EmpresaServiceImpl implements IEmpresaService {
    private final EmpresaRepository empresaRepository;
    private final PerfilRepository perfilRepository;
    private final EmpresaMapper mapper;

    private final VacanteRepository vacanteRepository;
    private final PostulacionRepository postulacionRepository;
    private final EntrevistaRepository entrevistaRepository;

    @Override
    @Transactional
    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO dto) {
        PerfilEntity perfil = perfilRepository.findById(dto.perfil_id())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado con ID: " + dto.perfil_id()));

        EmpresaEntity empresa = mapper.toEntity(dto);
        empresa.setPerfil(perfil);

        empresa = empresaRepository.save(empresa);

        return mapper.toDto(empresa);
    }

    @Override
    @Transactional
    public EmpresaResponseDTO editarEmpresa(Integer id, EmpresaRequestDTO dto) {
        EmpresaEntity empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));

        mapper.updateEntityFromDto(dto, empresa);

        empresa = empresaRepository.save(empresa);

        return mapper.toDto(empresa);
    }

    @Override
    public List<EmpresaResponseDTO> mostrarEmpresas() {
        return mapper.toDtoList(empresaRepository.findAll());
    }

    @Override
    public EmpresaResponseDTO buscarEmpresaXid(Integer id) {
        EmpresaEntity empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));
        return mapper.toDto(empresa);
    }

    @Override
    public List<EmpresaResponseDTO> buscarEmpresasXidPerfil(int idPerfil) {
        PerfilEntity perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado con ID: " + idPerfil));
        return empresaRepository.findByPerfil(perfil).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarEmpresa(Integer idEmpresa) {
        EmpresaEntity empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + idEmpresa));

        empresaRepository.delete(empresa);
    }

    @Override
    public DashboardStatsResponseDTO getDashboardStats(String username){
        EmpresaEntity empresa = empresaRepository.findByPerfilUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada para el usuario: " + username));

        Long totalVacantes = vacanteRepository.countByEmpresa_Id(empresa.getId());
        Long totalPostulaciones = postulacionRepository.countByVacante_Empresa_Id(empresa.getId());
        Long entrevistasPendientes = entrevistaRepository.countPendientesByEmpresaId(empresa.getId());
        Long contrataciones = 0L;

        return new DashboardStatsResponseDTO(totalVacantes, totalPostulaciones, entrevistasPendientes, contrataciones);
    }
}
