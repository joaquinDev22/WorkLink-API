package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EntrevistaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EntrevistaResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.mappers.EntrevistaMapper;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.EmpresaEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.EntrevistaEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.PostulacionVacanteEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.RecruiterEntity;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EntrevistaRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.PostulacionVacanteRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.RecruiterRepository;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IEntrevistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EntrevistaServiceImpl implements IEntrevistaService {

    private final EntrevistaRepository entrevistaRepository;
    private final PostulacionVacanteRepository postulacionVacanteRepository;
    private final EntrevistaMapper entrevistaMapper;
    private final EmpresaRepository empresaRepository;
    private final PerfilRepository perfilRepository;
    private final RecruiterRepository recruiterRepository;

    @Override
    public List<EntrevistaResponseDTO> listarEntrevistas() {
        return entrevistaMapper.toDtoList(entrevistaRepository.findAll());
    }

    @Override
    public List<EntrevistaResponseDTO> listarMisEntrevistas(String username) {
        // 1. Verificar si el usuario es la cuenta principal de la Empresa
        Optional<EmpresaEntity> empresaOpt = empresaRepository.findByPerfilUsuario(username);

        Integer empresaId = null;
        if (empresaOpt.isPresent()) {
            empresaId = empresaOpt.get().getId();
        } else {
            // 2. Si no es empresa directa, verificar si es un Recruiter de la empresa
            Optional<PerfilEntity> perfilOpt = perfilRepository.findByUsuario(username);
            if (perfilOpt.isPresent()) {
                Optional<RecruiterEntity> recruiterOpt = recruiterRepository.findByPerfil(perfilOpt.get());
                if (recruiterOpt.isPresent() && recruiterOpt.get().getEmpresa() != null) {
                    empresaId = recruiterOpt.get().getEmpresa().getId();
                }
            }
        }

        if (empresaId == null) {
            return Collections.emptyList();
        }

        // 3. Filtrar de forma segura únicamente las entrevistas de esta empresa
        List<EntrevistaEntity> entrevistas = entrevistaRepository.findByEmpresaId(empresaId);
        return entrevistaMapper.toDtoList(entrevistas);
    }

    @Override
    @Transactional
    public EntrevistaResponseDTO agendarEntrevista(EntrevistaRequestDTO dto, String username) {
        PostulacionVacanteEntity postulacion = postulacionVacanteRepository.findById(dto.postulacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada con ID: " + dto.postulacionId()));

        Integer empresaPostulacionId = postulacion.getVacante() != null && postulacion.getVacante().getEmpresa() != null
                ? postulacion.getVacante().getEmpresa().getId()
                : null;

        boolean autorizado = false;
        Optional<EmpresaEntity> empresaOpt = empresaRepository.findByPerfilUsuario(username);
        if (empresaOpt.isPresent() && empresaOpt.get().getId().equals(empresaPostulacionId)) {
            autorizado = true;
        } else {
            Optional<PerfilEntity> perfilOpt = perfilRepository.findByUsuario(username);
            if (perfilOpt.isPresent()) {
                Optional<RecruiterEntity> recruiterOpt = recruiterRepository.findByPerfil(perfilOpt.get());
                if (recruiterOpt.isPresent() && recruiterOpt.get().getEmpresa() != null &&
                    recruiterOpt.get().getEmpresa().getId().equals(empresaPostulacionId)) {
                    autorizado = true;
                }
            }
        }

        if (!autorizado) {
            throw new UnauthorizedAccessException("No tienes autorización para agendar entrevistas en esta postulación");
        }

        EntrevistaEntity entrevista = EntrevistaEntity.builder()
                .postulacion(postulacion)
                .fecha(dto.fecha())
                .hora(dto.hora())
                .link(dto.link())
                .build();

        EntrevistaEntity guardada = entrevistaRepository.save(entrevista);
        return entrevistaMapper.toDto(guardada);
    }
}
