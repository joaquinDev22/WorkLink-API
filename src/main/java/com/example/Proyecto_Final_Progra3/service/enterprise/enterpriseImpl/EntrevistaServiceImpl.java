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
import com.example.Proyecto_Final_Progra3.model.enterprise.EstadoEntrevista;
import com.example.Proyecto_Final_Progra3.model.enterprise.EstadoPostulacion;
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

// import java.time.LocalDate;
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
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada"));

        verificarPermiso(postulacion, username);

        // Una sola entrevista programada por postulación: para cambiarla se reprograma
        if (tieneEntrevistaProgramada(postulacion.getPostulacionVacanteId())) {
            throw new BusinessRuleException("Este postulante ya tiene una entrevista programada para esta vacante; reprográmala en lugar de agendar otra");
        }
        validarFechaDesdeManiana(dto);
        validarHorarioLibre(postulacion, dto, null);

        if (postulacion.getEstado() != null && postulacion.getEstado().esFinal()) {
            throw new BusinessRuleException("No se puede agendar una entrevista a una postulación rechazada");
        }
        // Agendar la entrevista hace avanzar la postulación (nunca la hace retroceder)
        if (postulacion.getEstado() == null || postulacion.getEstado().puedeAvanzarA(EstadoPostulacion.ENTREVISTA_AGENDADA)) {
            postulacion.setEstado(EstadoPostulacion.ENTREVISTA_AGENDADA);
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
                .orElseThrow(() -> new ResourceNotFoundException("Entrevista no encontrada"));
        PostulacionVacanteEntity postulacion = entrevista.getPostulacion();
        verificarPermiso(postulacion, username);

        if (!esProgramada(entrevista)) {
            throw new BusinessRuleException("Solo se puede reprogramar una entrevista que todavía no ocurrió");
        }
        validarFechaDesdeManiana(dto);
        if (dto.fecha().equals(entrevista.getFecha()) && dto.hora().equals(entrevista.getHora())) {
            throw new BusinessRuleException("La entrevista ya está en ese día y horario. Elige otro para reprogramarla.");
        }
        validarHorarioLibre(postulacion, dto, entrevista.getId());

        entrevista.setFecha(dto.fecha());
        entrevista.setHora(dto.hora());
        if (dto.link() != null && !dto.link().isBlank()) entrevista.setLink(dto.link());

        return guardarYNotificar(entrevista, postulacion, username);
    }

    @Override
    @Transactional
    public EntrevistaResponseDTO cancelarEntrevista(Integer entrevistaId, String username) {
        EntrevistaEntity entrevista = entrevistaRepository.findById(entrevistaId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrevista no encontrada"));

        PostulacionVacanteEntity postulacion = entrevista.getPostulacion();
        verificarPermiso(postulacion, username);

        // 1. Verificar que no estuviese cancelada desde antes
        if (entrevista.getStatus() == EstadoEntrevista.CANCELADA) {
            throw new BusinessRuleException("La entrevista ya se encuentra cancelada");
        }

        // 2. Verificar que la entrevista todavía no haya ocurrido
        if (!esProgramada(entrevista)) {
            throw new BusinessRuleException("Solo se puede cancelar una entrevista que todavía no ocurrió");
        }

        // 3. Verificar que no sea el mismo día de la entrevista
        if (entrevista.getFecha().isEqual(java.time.LocalDate.now())) {
            throw new BusinessRuleException("No se puede cancelar la entrevista el mismo día en que está programada");
        }

        entrevista.setStatus(EstadoEntrevista.CANCELADA);
        // Sin evento: EntrevistaAgendadaEvent se auditaría como "nueva entrevista agendada"
        return entrevistaMapper.toDto(entrevistaRepository.save(entrevista));
    }

    private static boolean esProgramada(EntrevistaEntity e) {
        return e.estaProgramada();
    }

    // Regla de negocio: nunca el mismo día ni antes; la entrevista va del día siguiente en adelante
    private static void validarFechaDesdeManiana(EntrevistaRequestDTO dto) {
        if (dto.fecha() == null || !dto.fecha().isAfter(java.time.LocalDate.now())) {
            throw new BusinessRuleException("La entrevista debe agendarse a partir del día siguiente");
        }
        if (dto.hora() == null || !EntrevistaEntity.esHorarioDeOficina(dto.fecha(), dto.hora())) {
            throw new BusinessRuleException("La entrevista debe ser en horario de oficina: de lunes a viernes, de 9 a 18 hs");
        }
    }

    // Dos entrevistas de la misma empresa no pueden caer en el mismo día y horario
    private void validarHorarioLibre(PostulacionVacanteEntity postulacion, EntrevistaRequestDTO dto, Integer excluirId) {
        Integer empresaId = postulacion.getVacante().getEmpresa().getId();
        if (entrevistaRepository.existeOtraEnHorario(empresaId, dto.fecha(), dto.hora(), excluirId)) {
            throw new BusinessRuleException("Ese horario ya está ocupado por otra entrevista. Elige otro día u hora.");
        }
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
