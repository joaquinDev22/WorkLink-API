package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.RecruiterRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.RecruiterResponseDTO;
import com.example.Proyecto_Final_Progra3.events.RecruiterVinculacionEvent;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruiterServiceImpl implements IRecruiterService {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final RecruiterRepository recruiterRepository;
    private final EmpresaRepository empresaRepository;
    private final PerfilRepository perfilRepository;
    private final RecruiterMapper recruiterMapper;
    private final ApplicationEventPublisher eventPublisher;

    private EmpresaEntity obtenerEmpresa(String usernameEmpresa) {
        PerfilEntity perfil = perfilRepository.findByUsuario(usernameEmpresa)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + usernameEmpresa));

        return empresaRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("El usuario autenticado no posee una empresa registrada"));
    }

    private RecruiterEntity obtenerRecruiterDeEmpresa(String usernameEmpresa, Integer recruiterId) {
        EmpresaEntity empresa = obtenerEmpresa(usernameEmpresa);
        return recruiterRepository.findByRecruiterIdAndEmpresa_Id(recruiterId, empresa.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter no encontrado"));
    }

    /** Cuenta RECRUITER registrada con ese email y todavía sin empresa. */
    private PerfilEntity obtenerCuentaRecruiterLibre(String email) {
        PerfilEntity perfil = perfilRepository.findByEmail(email)
                .filter(p -> "RECRUITER".equalsIgnoreCase(p.getRol().getNombre()))
                .orElseThrow(() -> new BusinessRuleException(
                        "No existe una cuenta de WorkLink con rol Recruiter y email " + email
                                + ". El recruiter debe registrarse primero con ese email."));
        if (recruiterRepository.findByPerfil(perfil).isPresent()) {
            throw new BusinessRuleException("La cuenta " + email + " ya está asociada a una empresa");
        }
        return perfil;
    }

    @Override
    @Transactional
    public RecruiterResponseDTO crearRecruiter(String usernameEmpresa, RecruiterRequestDTO dto) {
        EmpresaEntity empresa = obtenerEmpresa(usernameEmpresa);
        String email = dto.email().trim().toLowerCase();

        if (recruiterRepository.existsByEmail(email)) {
            throw new BusinessRuleException("Ya existe un recruiter registrado con el email: " + email);
        }

        // RF01 + RF11: el recruiter se registra solo y la empresa lo vincula únicamente por email.
        // Nombre y demás datos salen de su cuenta: la empresa no los carga.
        PerfilEntity perfil = obtenerCuentaRecruiterLibre(email);

        RecruiterEntity recruiter = RecruiterEntity.builder()
                .email(email)
                .nombre(perfil.getUsuario())
                .apellido("")
                .empresa(empresa)
                .perfil(perfil)
                .fechaVinculacion(LocalDateTime.now())
                .vinculacionVista(false)
                .build();

        RecruiterEntity guardado = recruiterRepository.save(recruiter);
        publicar(guardado, RecruiterVinculacionEvent.Tipo.VINCULADO);
        return recruiterMapper.toDto(guardado);
    }

    @Override
    public List<RecruiterResponseDTO> listarRecruitersPorEmpresa(String usernameEmpresa) {
        EmpresaEntity empresa = obtenerEmpresa(usernameEmpresa);
        return recruiterMapper.toDtoList(recruiterRepository.findByEmpresa_Id(empresa.getId()));
    }

    @Override
    public RecruiterResponseDTO buscarPorId(String usernameEmpresa, Integer recruiterId) {
        return recruiterMapper.toDto(obtenerRecruiterDeEmpresa(usernameEmpresa, recruiterId));
    }

    @Override
    @Transactional
    public RecruiterResponseDTO actualizarRecruiter(String usernameEmpresa, Integer recruiterId, RecruiterRequestDTO dto) {
        RecruiterEntity recruiter = obtenerRecruiterDeEmpresa(usernameEmpresa, recruiterId);
        String email = dto.email().trim().toLowerCase();

        if (email.equalsIgnoreCase(recruiter.getEmail())) {
            return recruiterMapper.toDto(recruiter);
        }
        if (recruiterRepository.existsByEmailAndRecruiterIdNot(email, recruiterId)) {
            throw new BusinessRuleException("El email ingresado ya pertenece a otro recruiter: " + email);
        }

        // Cambiar el email equivale a vincular otra cuenta: tiene que existir y estar libre
        PerfilEntity perfil = obtenerCuentaRecruiterLibre(email);
        recruiter.setEmail(email);
        recruiter.setPerfil(perfil);
        recruiter.setNombre(perfil.getUsuario());
        recruiter.setFechaVinculacion(LocalDateTime.now());
        recruiter.setVinculacionVista(false);
        recruiter.setBajaSolicitadaEn(null);

        RecruiterEntity actualizado = recruiterRepository.save(recruiter);
        publicar(actualizado, RecruiterVinculacionEvent.Tipo.VINCULADO);
        return recruiterMapper.toDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminarRecruiter(String usernameEmpresa, Integer recruiterId) {
        RecruiterEntity recruiter = obtenerRecruiterDeEmpresa(usernameEmpresa, recruiterId);
        publicar(recruiter, RecruiterVinculacionEvent.Tipo.DESVINCULADO_POR_EMPRESA);
        recruiterRepository.delete(recruiter);
    }

    @Override
    @Transactional
    public void aceptarBaja(String usernameEmpresa, Integer recruiterId) {
        RecruiterEntity recruiter = obtenerRecruiterDeEmpresa(usernameEmpresa, recruiterId);
        if (!recruiter.tieneBajaSolicitada()) {
            throw new BusinessRuleException("Este recruiter no solicitó la baja");
        }
        publicar(recruiter, RecruiterVinculacionEvent.Tipo.BAJA_ACEPTADA);
        recruiterRepository.delete(recruiter);
    }

    // ---------- Lado del recruiter ----------

    private RecruiterEntity obtenerMiVinculacion(String usernameRecruiter) {
        PerfilEntity perfil = perfilRepository.findByUsuario(usernameRecruiter)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + usernameRecruiter));
        return recruiterRepository.findByPerfil(perfil)
                .filter(r -> r.getEmpresa() != null)
                .orElseThrow(() -> new ResourceNotFoundException("No estás vinculado a ninguna empresa"));
    }

    @Override
    public RecruiterResponseDTO miVinculacion(String usernameRecruiter) {
        return recruiterMapper.toDto(obtenerMiVinculacion(usernameRecruiter));
    }

    @Override
    @Transactional
    public RecruiterResponseDTO marcarVinculacionVista(String usernameRecruiter) {
        RecruiterEntity recruiter = obtenerMiVinculacion(usernameRecruiter);
        recruiter.setVinculacionVista(true);
        return recruiterMapper.toDto(recruiterRepository.save(recruiter));
    }

    @Override
    @Transactional
    public RecruiterResponseDTO solicitarBaja(String usernameRecruiter) {
        RecruiterEntity recruiter = obtenerMiVinculacion(usernameRecruiter);
        if (recruiter.tieneBajaSolicitada()) {
            throw new BusinessRuleException("Ya solicitaste la baja el " + recruiter.getBajaSolicitadaEn().format(FECHA)
                    + ". Si la empresa no responde, puedes desvincularte a partir del " + recruiter.fechaBajaHabilitada().format(FECHA) + ".");
        }
        recruiter.setBajaSolicitadaEn(LocalDateTime.now());
        RecruiterEntity guardado = recruiterRepository.save(recruiter);
        publicar(guardado, RecruiterVinculacionEvent.Tipo.BAJA_SOLICITADA);
        return recruiterMapper.toDto(guardado);
    }

    @Override
    @Transactional
    public RecruiterResponseDTO cancelarSolicitudBaja(String usernameRecruiter) {
        RecruiterEntity recruiter = obtenerMiVinculacion(usernameRecruiter);
        if (!recruiter.tieneBajaSolicitada()) {
            throw new BusinessRuleException("No tienes una solicitud de baja pendiente");
        }
        recruiter.setBajaSolicitadaEn(null);
        return recruiterMapper.toDto(recruiterRepository.save(recruiter));
    }

    @Override
    @Transactional
    public void confirmarBajaPorCuentaPropia(String usernameRecruiter) {
        RecruiterEntity recruiter = obtenerMiVinculacion(usernameRecruiter);
        if (!recruiter.tieneBajaSolicitada()) {
            throw new BusinessRuleException("Primero tienes que solicitar la baja a la empresa");
        }
        if (!recruiter.puedeDarseDeBajaSolo(LocalDateTime.now())) {
            throw new BusinessRuleException("La empresa tiene " + RecruiterEntity.DIAS_RESPUESTA_BAJA
                    + " días para responder. Puedes desvincularte por tu cuenta a partir del "
                    + recruiter.fechaBajaHabilitada().format(FECHA) + ".");
        }
        publicar(recruiter, RecruiterVinculacionEvent.Tipo.BAJA_POR_CUENTA_PROPIA);
        recruiterRepository.delete(recruiter);
    }

    // Patrón GoF Observer: auditoría y avisos desacoplados del servicio
    private void publicar(RecruiterEntity recruiter, RecruiterVinculacionEvent.Tipo tipo) {
        eventPublisher.publishEvent(new RecruiterVinculacionEvent(
                tipo,
                recruiter.getRecruiterId(),
                recruiter.getEmail(),
                recruiter.getEmpresa() != null ? recruiter.getEmpresa().getNombre() : null
        ));
    }
}
