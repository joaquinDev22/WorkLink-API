package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.RecruiterRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.RecruiterResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.mappers.RecruiterMapper;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.EmpresaEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.RecruiterEntity;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.RecruiterRepository;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IRecruiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruiterServiceImpl implements IRecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final EmpresaRepository empresaRepository;
    private final PerfilRepository perfilRepository;
    private final RecruiterMapper recruiterMapper;

    private EmpresaEntity obtenerEmpresa(String usernameEmpresa) {
        PerfilEntity perfil = perfilRepository.findByUsuario(usernameEmpresa)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + usernameEmpresa));

        return empresaRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("El usuario autenticado no posee una empresa registrada"));
    }

    @Override
    @Transactional
    public RecruiterResponseDTO crearRecruiter(String usernameEmpresa, RecruiterRequestDTO dto) {
        EmpresaEntity empresa = obtenerEmpresa(usernameEmpresa);

        if (recruiterRepository.existsByEmail(dto.email())) {
            throw new BusinessRuleException("Ya existe un recruiter registrado con el email: " + dto.email());
        }

        RecruiterEntity recruiter = recruiterMapper.toEntity(dto);
        recruiter.setEmpresa(empresa);

        RecruiterEntity guardado = recruiterRepository.save(recruiter);
        return recruiterMapper.toDto(guardado);
    }

    @Override
    public List<RecruiterResponseDTO> listarRecruitersPorEmpresa(String usernameEmpresa) {
        EmpresaEntity empresa = obtenerEmpresa(usernameEmpresa);
        return recruiterMapper.toDtoList(recruiterRepository.findByEmpresa_Id(empresa.getId()));
    }

    @Override
    public RecruiterResponseDTO buscarPorId(String usernameEmpresa, Integer recruiterId) {
        EmpresaEntity empresa = obtenerEmpresa(usernameEmpresa);
        RecruiterEntity recruiter = recruiterRepository.findByRecruiterIdAndEmpresa_Id(recruiterId, empresa.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter no encontrado con ID: " + recruiterId));
        return recruiterMapper.toDto(recruiter);
    }

    @Override
    @Transactional
    public RecruiterResponseDTO actualizarRecruiter(String usernameEmpresa, Integer recruiterId, RecruiterRequestDTO dto) {
        EmpresaEntity empresa = obtenerEmpresa(usernameEmpresa);
        RecruiterEntity recruiter = recruiterRepository.findByRecruiterIdAndEmpresa_Id(recruiterId, empresa.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter no encontrado con ID: " + recruiterId));

        if (recruiterRepository.existsByEmailAndRecruiterIdNot(dto.email(), recruiterId)) {
            throw new BusinessRuleException("El email ingresado ya pertenece a otro recruiter: " + dto.email());
        }

        recruiterMapper.updateEntityFromDto(dto, recruiter);
        RecruiterEntity actualizado = recruiterRepository.save(recruiter);
        return recruiterMapper.toDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminarRecruiter(String usernameEmpresa, Integer recruiterId) {
        EmpresaEntity empresa = obtenerEmpresa(usernameEmpresa);
        RecruiterEntity recruiter = recruiterRepository.findByRecruiterIdAndEmpresa_Id(recruiterId, empresa.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter no encontrado con ID: " + recruiterId));

        recruiterRepository.delete(recruiter);
    }
}

