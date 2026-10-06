package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.responsedto.FreelancerFinancesResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.FreelancerTransactionResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.pagos.PagosEntity;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.PagosRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                        p.getProyecto() != null ? p.getProyecto().getArea() : "Proyecto"
                ))
                .toList();

        return ResponseEntity.ok(response);
    }
}
