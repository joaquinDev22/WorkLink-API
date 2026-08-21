package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.mappers.EmpresaMapper;
import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.RecruiterRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.VacanteRepository;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IEmpresaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class EmpresaServiceImpl implements IEmpresaService {
    private final EmpresaRepository empresaRepository;
    private final PerfilRepository perfilRepository;
    private final EmpresaMapper mapper;
    
    private final RecruiterRepository recruiterRepository;
    private final VacanteRepository vacanteRepository;
    private final InstructorRepository instructorRepository;
    private final BootcampRepository bootcampRepository;

    @Override
    @Transactional
    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO dto) {
        Perfil perfil = perfilRepository.findById(dto.perfil_id())
                .orElseThrow(() -> new RuntimeException("Ese perfil no existe"));

        Empresa empresa = mapper.toEntity(dto);
        empresa.setPerfil(perfil);

        empresa = empresaRepository.save(empresa);

        return mapper.toDto(empresa);
    }

    @Override
    @Transactional
    public EmpresaResponseDTO editarEmpresa(Integer id, EmpresaRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Esa empresa no existe"));

        mapper.updateEntityFromDto(dto, empresa);

        empresa = empresaRepository.save(empresa);

        return mapper.toDto(empresa);
    }

    @Override
    public List<EmpresaResponseDTO> mostrarEmpresas() {
        return empresaRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmpresaResponseDTO buscarEmpresaXid(Integer id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ese empresa no existe"));
        return mapper.toDto(empresa);
    }

    @Override
    public List<EmpresaResponseDTO> buscarEmpresasXidPerfil(int idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RuntimeException("Ese perfil no existe"));
        return empresaRepository.findByPerfil(perfil).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarEmpresa(Integer idEmpresa) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Ese empresa no existe"));

        empresaRepository.delete(empresa);
    }

    @Override
    public Map<String, Long> getDashboardStats(String username) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Ese perfil no existe"));

        Empresa empresa = empresaRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Esa empresa no existe"));

        Integer empresaId = empresa.getId();

        Long recruiters = recruiterRepository.countByEmpresa_Id(empresaId);
        Long instructores = instructorRepository.countByEmpresa_Id(empresaId);
        Long vacantes = vacanteRepository.countByEmpresa_Id(empresaId);
        Long bootcamps = bootcampRepository.countByEmpresa_Id(empresaId);

        Map<String, Long> stats = new HashMap<>();
        stats.put("recruiters", recruiters);
        stats.put("instructores", instructores);
        stats.put("vacantes", vacantes);
        stats.put("bootcamps", bootcamps);

        return stats;
    }
}
