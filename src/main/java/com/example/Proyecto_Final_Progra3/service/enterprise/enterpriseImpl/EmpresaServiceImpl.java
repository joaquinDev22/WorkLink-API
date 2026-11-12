package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.DashboardStatsResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.mappers.EmpresaMapper;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.EmpresaEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.RecruiterEntity;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.*;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IEmpresaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Proyecto_Final_Progra3.exceptions.domain.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    private final RecruiterRepository recruiterRepository;

    @Override
    @Transactional
    public EmpresaResponseDTO crearEmpresa(String username, EmpresaRequestDTO dto) {
        // El perfil sale del token: dto.perfil_id() se ignora para que nadie cree empresas a nombre de otro
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
        if (empresaRepository.findByPerfil(perfil).isPresent()) {
            throw new BusinessRuleException("Tu cuenta ya tiene una empresa registrada; editala en lugar de crear otra");
        }

        EmpresaEntity empresa = mapper.toEntity(dto);
        empresa.setPerfil(perfil);

        empresa = empresaRepository.save(empresa);

        return mapper.toDto(empresa);
    }

    @Override
    @Transactional
    public EmpresaResponseDTO editarEmpresa(Integer id, String username, EmpresaRequestDTO dto) {
        EmpresaEntity empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));

        // Validación anti-IDOR: solo la empresa titular puede editar su perfil
        if (empresa.getPerfil() == null || !empresa.getPerfil().getUsuario().equals(username)) {
            throw new UnauthorizedAccessException("No tienes permiso para modificar la información de esta empresa");
        }

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
    public EmpresaResponseDTO buscarEmpresaXuuid(UUID uuid) {
        EmpresaEntity empresa = empresaRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con UUID: " + uuid));
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
    public void eliminarEmpresa(Integer idEmpresa, String username) {
        EmpresaEntity empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + idEmpresa));

        // Validación anti-IDOR
        if (empresa.getPerfil() == null || !empresa.getPerfil().getUsuario().equals(username)) {
            throw new UnauthorizedAccessException("No tienes permiso para eliminar esta empresa");
        }

        empresaRepository.delete(empresa);
    }

    private EmpresaEntity resolverEmpresaPorUsuario(String username) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
        EmpresaEntity empresa = empresaRepository.findByPerfil(perfil).orElse(null);
        if (empresa == null) {
            RecruiterEntity recruiter = recruiterRepository.findByPerfil(perfil).orElse(null);
            if (recruiter != null) {
                empresa = recruiter.getEmpresa();
            }
        }
        if (empresa == null) {
            throw new ResourceNotFoundException("No se encontró una empresa asociada al usuario: " + username);
        }
        return empresa;
    }

    @Override
    public EmpresaResponseDTO buscarMiEmpresa(String username) {
        return mapper.toDto(resolverEmpresaPorUsuario(username));
    }

    @Override
    public DashboardStatsResponseDTO getDashboardStats(String username) {
        EmpresaEntity empresa = resolverEmpresaPorUsuario(username);

        Long totalVacantes = vacanteRepository.countByEmpresa_Id(empresa.getId());
        Long totalPostulaciones = postulacionRepository.countByVacante_Empresa_Id(empresa.getId());
        Long entrevistasPendientes = entrevistaRepository.countPendientesByEmpresaId(empresa.getId());
        Long contrataciones = 0L;

        return new DashboardStatsResponseDTO(totalVacantes, totalPostulaciones, entrevistasPendientes, contrataciones);
    }

    private static final Set<String> TIPOS_IMAGEN = Set.of("image/png", "image/jpeg", "image/webp");

    @Override
    @Transactional
    public String subirLogo(String username, MultipartFile file) {
        EmpresaEntity empresa = miEmpresa(username);
        empresa.setLogo(aDataUrl(file, 5, "logo"));
        return empresaRepository.save(empresa).getLogo();
    }

    @Override
    @Transactional
    public String subirBanner(String username, MultipartFile file) {
        EmpresaEntity empresa = miEmpresa(username);
        empresa.setBanner(aDataUrl(file, 8, "banner"));
        return empresaRepository.save(empresa).getBanner();
    }

    private EmpresaEntity miEmpresa(String username) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
        return empresaRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("El usuario autenticado no posee una empresa registrada"));
    }

    // Valida tipo y tamaño y devuelve la imagen como data URL lista para <img src>
    private static String aDataUrl(MultipartFile file, int maxMb, String campo) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("No se recibió ningún archivo para el " + campo);
        }
        if (!TIPOS_IMAGEN.contains(file.getContentType())) {
            throw new BadRequestException("El " + campo + " debe ser una imagen PNG, JPG o WEBP (se recibió " + file.getContentType() + ")");
        }
        if (file.getSize() > maxMb * 1024L * 1024L) {
            throw new BadRequestException("El " + campo + " no puede superar los " + maxMb + " MB");
        }
        try {
            return "data:" + file.getContentType() + ";base64," + Base64.getEncoder().encodeToString(file.getBytes());
        } catch (IOException e) {
            throw new BadRequestException("No se pudo leer el archivo del " + campo + ": " + e.getMessage());
        }
    }
}
