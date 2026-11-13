package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.MedioCobroRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.FreelancerFinancesResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.FreelancerTransactionResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.MedioCobroResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ReputacionPerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.pagos.PagosEntity;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.PagosRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.ICalificacionProyectoService;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IFreelancerMedioCobroService;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IProyectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/freelancer")
@RequiredArgsConstructor
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class FreelancerController {

    private final PerfilRepository perfilRepository;
    private final EmployeeRepository employeeRepository;
    private final ProyectoRepository proyectoRepository;
    private final PagosRepository pagosRepository;
    private final IFreelancerMedioCobroService medioCobroService;
    private final IProyectoService proyectoService;
    private final ICalificacionProyectoService calificacionService;

    private EmployeeEntity obtenerFreelancer(String username) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        return employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer no encontrado para el usuario: " + username));
    }

    @Operation(summary = "Consultar finanzas", description = "Obtiene el balance financiero real del freelancer autenticado calculado desde la base de datos MySQL.")
    @GetMapping("/finances")
    public ResponseEntity<FreelancerFinancesResponseDTO> getFinances(Authentication authentication) {
        EmployeeEntity freelancer = obtenerFreelancer(authentication.getName());
        Integer freelancerId = freelancer.getEmployeeId();

        Double balance = pagosRepository.sumMontoByFreelancerId(freelancerId);
        Double pending = proyectoRepository.sumPresupuestoByFreelancerAndEstados(freelancerId, List.of(EstadoProyecto.EN_PROGRESO));

        return ResponseEntity.ok(new FreelancerFinancesResponseDTO(
                balance != null ? balance : 0.0,
                pending != null ? pending : 0.0,
                balance != null ? balance : 0.0
        ));
    }

    @Operation(summary = "Consultar transacciones", description = "Obtiene el historial real de pagos registrados para los proyectos asignados al freelancer autenticado.")
    @GetMapping("/transactions")
    public ResponseEntity<List<FreelancerTransactionResponseDTO>> getTransactions(Authentication authentication) {
        EmployeeEntity freelancer = obtenerFreelancer(authentication.getName());

        List<PagosEntity> pagos = pagosRepository.findByProyecto_Freelancer_EmployeeId(freelancer.getEmployeeId());

        List<FreelancerTransactionResponseDTO> response = pagos.stream()
                .map(p -> new FreelancerTransactionResponseDTO(
                        p.getPagosId(),
                        p.getMoneda() != null ? p.getMoneda().name() : "PESOS",
                        p.getMonto(),
                        p.getMedioDePago() != null ? p.getMedioDePago().name() : "BILLETERA_VIRTUAL",
                        p.getProyecto() != null ? p.getProyecto().getId() : null,
                        p.getProyecto() != null ? p.getProyecto().getNombreProyecto() : "Proyecto"
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar medios de cobro", description = "Obtiene los datos bancarios o billetera configurados por el freelancer autenticado. Devuelve 204 si aún no configuró.")
    @GetMapping("/medios-cobro")
    @PreAuthorize("hasAnyRole('FREELANCER', 'EMPLOYEE', 'POSTULANTE')")
    public ResponseEntity<MedioCobroResponseDTO> getMedioCobro(Authentication authentication) {
        MedioCobroResponseDTO response = medioCobroService.getMedioCobro(authentication.getName());
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Configurar medio de cobro", description = "Permite registrar o actualizar los datos bancarios o de Mercado Pago del freelancer autenticado (upsert).")
    @PutMapping("/medios-cobro")
    @PreAuthorize("hasAnyRole('FREELANCER', 'EMPLOYEE', 'POSTULANTE')")
    public ResponseEntity<MedioCobroResponseDTO> upsertMedioCobro(
            Authentication authentication,
            @Valid @RequestBody MedioCobroRequestDTO request) {
        return ResponseEntity.ok(medioCobroService.upsertMedioCobro(authentication.getName(), request));
    }

    @Operation(summary = "Eliminar medio de cobro", description = "Elimina los datos bancarios configurados por el freelancer autenticado.")
    @DeleteMapping("/medios-cobro")
    @PreAuthorize("hasAnyRole('FREELANCER', 'EMPLOYEE', 'POSTULANTE')")
    public ResponseEntity<Void> deleteMedioCobro(Authentication authentication) {
        medioCobroService.deleteMedioCobro(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar proyectos asignados del freelancer", description = "Obtiene todos los proyectos asignados al freelancer autenticado (histórico y activos).")
    @GetMapping({"/proyectos", "/proyectos/historico", "/proyectos/asignados"})
    @PreAuthorize("hasAnyRole('FREELANCER', 'EMPLOYEE', 'POSTULANTE')")
    public ResponseEntity<List<ProyectoResponseDTO>> getMisProyectos(Authentication authentication) {
        return ResponseEntity.ok(proyectoService.listarMisProyectosAsignados(authentication.getName()));
    }

    @Operation(summary = "Obtener opiniones y calificaciones del freelancer", description = "Obtiene el promedio y la lista de calificaciones/reseñas históricas recibidas.")
    @GetMapping({"/opiniones", "/calificaciones"})
    @PreAuthorize("hasAnyRole('FREELANCER', 'EMPLOYEE', 'POSTULANTE')")
    public ResponseEntity<ReputacionPerfilResponseDTO> getMisOpiniones(Authentication authentication) {
        return ResponseEntity.ok(calificacionService.obtenerReputacionPorUsuario(authentication.getName()));
    }
}
