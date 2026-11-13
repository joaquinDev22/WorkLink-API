package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EntrevistaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EntrevistaResponseDTO;
import com.example.Proyecto_Final_Progra3.events.EntrevistaAgendadaEvent;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ApplicationEventPublisher eventPublisher;

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

        if (empresaId != null) {
            // Empresa o Recruiter: listar entrevistas de las vacantes de la empresa
            List<EntrevistaEntity> entrevistas = entrevistaRepository.findByEmpresaId(empresaId);
            return entrevistaMapper.toDtoList(entrevistas);
        }

        // 3. Si no es empresa ni recruiter, listar las entrevistas del postulante
        List<EntrevistaEntity> entrevistasPostulante = entrevistaRepository.findByPostulanteUsuario(username);
        return entrevistaMapper.toDtoList(entrevistasPostulante);
    }

    @Override
    @Transactional
    public EntrevistaResponseDTO agendarEntrevista(EntrevistaRequestDTO dto, String username) {
        PostulacionVacanteEntity postulacion = postulacionVacanteRepository.findById(dto.postulacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada con ID: " + dto.postulacionId()));

        verificarPermiso(postulacion, username);

        // Una sola entrevista programada por postulación: para cambiarla se reprograma
        if (tieneEntrevistaProgramada(postulacion.getPostulacionVacanteId())) {
            throw new BusinessRuleException("Este postulante ya tiene una entrevista programada para esta vacante; reprogramala en lugar de agendar otra");
        }

        EntrevistaEntity entrevista = EntrevistaEntity.builder()
                .postulacion(postulacion)
                .fecha(dto.fecha())
                .hora(dto.hora())
                .link(dto.link())
                .build();

        return guardarYNotificar(entrevista, postulacion, username);
    }

    @Override
    @Transactional
    public EntrevistaResponseDTO reprogramarEntrevista(Integer entrevistaId, EntrevistaRequestDTO dto, String username) {
        EntrevistaEntity entrevista = entrevistaRepository.findById(entrevistaId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrevista no encontrada con ID: " + entrevistaId));
        PostulacionVacanteEntity postulacion = entrevista.getPostulacion();
        verificarPermiso(postulacion, username);

        if (!esProgramada(entrevista)) {
            throw new BusinessRuleException("Solo se puede reprogramar una entrevista que todavía no ocurrió");
        }

        entrevista.setFecha(dto.fecha());
        entrevista.setHora(dto.hora());
        if (dto.link() != null && !dto.link().isBlank()) entrevista.setLink(dto.link());

        return guardarYNotificar(entrevista, postulacion, username);
    }

    private static boolean esProgramada(EntrevistaEntity e) {
        return e.getFecha() != null && e.getHora() != null
                && !java.time.LocalDateTime.of(e.getFecha(), e.getHora()).isBefore(java.time.LocalDateTime.now());
    }

    private boolean tieneEntrevistaProgramada(Integer postulacionId) {
        return entrevistaRepository.findByPostulacion_PostulacionVacanteId(postulacionId).stream()
                .anyMatch(EntrevistaServiceImpl::esProgramada);
    }

    private void verificarPermiso(PostulacionVacanteEntity postulacion, String username) {
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
            throw new UnauthorizedAccessException("No tienes autorización para gestionar entrevistas en esta postulación");
        }
    }

    private EntrevistaResponseDTO guardarYNotificar(EntrevistaEntity entrevista, PostulacionVacanteEntity postulacion, String username) {
        EntrevistaEntity guardada = entrevistaRepository.save(entrevista);

        // Patrón GoF Observer: publicar evento de dominio para auditoría y notificaciones desacopladas
        String postulanteEmail = (postulacion.getEmployee() != null && postulacion.getEmployee().getEmail() != null)
                ? postulacion.getEmployee().getEmail()
                : (postulacion.getEmployee() != null && postulacion.getEmployee().getPerfil() != null ? postulacion.getEmployee().getPerfil().getEmail() : "N/A");

        eventPublisher.publishEvent(new EntrevistaAgendadaEvent(
                guardada.getId(),
                postulacion.getPostulacionVacanteId(),
                username,
                postulanteEmail,
                guardada.getFecha(),
                guardada.getHora(),
                guardada.getLink()
        ));

        return entrevistaMapper.toDto(guardada);
    }
}
