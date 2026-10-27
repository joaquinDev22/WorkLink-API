package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.ClientProfileRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ClientDashboardResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ClientProfileResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.ClientEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoProyecto;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ClientRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.PagosRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.PostulacionProyectoRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IProyectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
@Tag(name = "Freelance", description = "Gestión de proyectos freelance, postulaciones, hitos, pagos, clientes y freelancers.")
public class ClientController {

    private final PerfilRepository perfilRepository;
    private final ClientRepository clientRepository;
    private final ProyectoRepository proyectoRepository;
    private final PagosRepository pagosRepository;
    private final PostulacionProyectoRepository postulacionProyectoRepository;
    private final IProyectoService proyectoService;

    @Operation(summary = "Obtener dashboard del cliente", description = "Obtiene las métricas estadísticas reales del cliente autenticado directamente desde la base de datos MySQL.")
    @GetMapping("/dashboard")
    public ResponseEntity<ClientDashboardResponseDTO> getDashboard(Authentication authentication) {
        PerfilEntity perfil = perfilRepository.findByUsuario(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + authentication.getName()));

        ClientEntity client = clientRepository.findByPerfil(perfil)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado para el usuario: " + authentication.getName()));

        Integer clientId = client.getClienteId();
        long activeProjects = proyectoRepository.countByClient_ClienteIdAndEstadoIn(clientId, List.of(EstadoProyecto.BUSCANDO_TALENTO, EstadoProyecto.EN_PROGRESO));
        long completedProjects = proyectoRepository.countByClient_ClienteIdAndEstado(clientId, EstadoProyecto.FINALIZADO);
        Double totalSpent = pagosRepository.sumMontoByClienteId(clientId);
        long pendingProposals = postulacionProyectoRepository.countByProyecto_Client_ClienteId(clientId);

        return ResponseEntity.ok(new ClientDashboardResponseDTO(
                activeProjects,
                completedProjects,
                totalSpent != null ? totalSpent : 0.0,
                pendingProposals
        ));
    }

    @Operation(summary = "Listar proyectos del cliente", description = "Obtiene la lista real de proyectos publicados por el cliente autenticado desde la base de datos.")
    @GetMapping("/proyectos")
    public ResponseEntity<List<ProyectoResponseDTO>> getProyectos(Authentication authentication) {
        return ResponseEntity.ok(proyectoService.listarMisProyectosPublicados(authentication.getName()));
    }

    @Operation(summary = "Obtener perfil del cliente", description = "Obtiene los datos personales del cliente particular autenticado.")
    @GetMapping("/me")
    public ResponseEntity<ClientProfileResponseDTO> getMyClientProfile(Authentication authentication) {
        PerfilEntity perfil = perfilRepository.findByUsuario(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + authentication.getName()));

        ClientEntity client = clientRepository.findByPerfil(perfil)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado para el usuario: " + authentication.getName()));

        return ResponseEntity.ok(mapToClientDto(client));
    }

    @Operation(summary = "Actualizar perfil del cliente", description = "Permite modificar datos de contacto y país del cliente autenticado.")
    @PutMapping("/me")
    public ResponseEntity<ClientProfileResponseDTO> updateMyClientProfile(
            Authentication authentication,
            @RequestBody ClientProfileRequestDTO dto) {
        PerfilEntity perfil = perfilRepository.findByUsuario(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + authentication.getName()));

        ClientEntity client = clientRepository.findByPerfil(perfil)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado para el usuario: " + authentication.getName()));

        if (dto.firstName() != null) client.setFirstName(dto.firstName());
        if (dto.lastName() != null) client.setLastName(dto.lastName());
        if (dto.phoneNumber() != null) client.setPhoneNumber(dto.phoneNumber());
        if (dto.country() != null) client.setCountry(dto.country());
        if (dto.fechaNacimiento() != null) client.setFechaNacimiento(dto.fechaNacimiento());

        ClientEntity guardado = clientRepository.save(client);
        return ResponseEntity.ok(mapToClientDto(guardado));
    }

    private ClientProfileResponseDTO mapToClientDto(ClientEntity client) {
        return new ClientProfileResponseDTO(
                client.getClienteId(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getCountry(),
                client.getFechaNacimiento(),
                client.getPerfil() != null ? client.getPerfil().getId() : null,
                client.getPerfil() != null ? client.getPerfil().getUsuario() : null
        );
    }
}
