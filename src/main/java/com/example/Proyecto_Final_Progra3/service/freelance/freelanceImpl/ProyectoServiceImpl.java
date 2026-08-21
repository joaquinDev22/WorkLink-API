package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.ProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.Client;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.Proyecto;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ClientRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoServiceImpl implements IProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final ClientRepository clientRepository;
    private final PerfilRepository perfilRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public ProyectoResponseDTO crearProyecto(ProyectoRequestDTO dto) {
        Client client = clientRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Proyecto proyecto = new Proyecto();
        proyecto.setArea(dto.area());
        proyecto.setDescripcion(dto.descripcion());
        proyecto.setFechaPublicacion(LocalDate.now());
        proyecto.setPresupuestoMax(dto.presupuestoMax());
        proyecto.setPresupuestoMin(dto.presupuestoMin());
        proyecto.setClient(client);

        proyecto.setDuracion(dto.duracion());
        proyecto.setTipoPago(dto.tipoPago());
        proyecto.setHabilidades(dto.habilidades());

        Proyecto guardado = proyectoRepository.save(proyecto);
        return mapToDto(guardado);
    }

    @Override
    @Transactional
    public ProyectoResponseDTO crearMiProyecto(String username, ProyectoRequestDTO dto) {
        com.example.Proyecto_Final_Progra3.model.Perfil perfil = perfilRepository
                .findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Client client = clientRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo los clientes pueden crear proyectos"));

        ProyectoRequestDTO dtoConCliente = new ProyectoRequestDTO(
                dto.area(),
                dto.descripcion(),
                dto.presupuestoMax(),
                dto.presupuestoMin(),
                client.getClienteId(),
                dto.duracion(),
                dto.tipoPago(),
                dto.habilidades()
        );
        return crearProyecto(dtoConCliente);
    }

    @Override
    @Transactional
    public void cancelarMiProyecto(Integer id, String username) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getClient().getPerfil().getUsuario().equals(username)) {
            throw new RuntimeException("No tienes permiso para cancelar este proyecto");
        }

        if (proyecto.getEstado() != com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoProyecto.BUSCANDO_TALENTO) {
            throw new RuntimeException("Solo puedes cancelar proyectos que estén buscando talento");
        }

        proyecto.setEstado(com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoProyecto.CANCELADO);
        proyectoRepository.save(proyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoResponseDTO buscarPorId(Integer id) {
        return proyectoRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponseDTO> listarTodos() {
        return proyectoRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponseDTO> listarMisProyectosPublicados(String username) {
        com.example.Proyecto_Final_Progra3.model.Perfil perfil = perfilRepository
                .findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Client client = clientRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return proyectoRepository.findByClient_ClienteId(client.getClienteId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponseDTO> listarMisProyectosAsignados(String username) {
        com.example.Proyecto_Final_Progra3.model.Perfil perfil = perfilRepository
                .findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        com.example.Proyecto_Final_Progra3.model.employee.Employee employee = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Freelancer no encontrado"));

        return proyectoRepository.findByFreelancer_EmployeeId(employee.getEmployeeId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ProyectoResponseDTO mapToDto(Proyecto proyecto) {
        Integer clienteId = proyecto.getClient() != null ? proyecto.getClient().getClienteId() : null;
        String nombreCliente = proyecto.getClient() != null
                ? (proyecto.getClient().getFirstName() + " " + proyecto.getClient().getLastName())
                : "Cliente Desconocido";
        String estado = proyecto.getEstado() != null ? proyecto.getEstado().name() : "BUSCANDO_TALENTO";
        String nombreFreelancer = proyecto.getFreelancer() != null
                ? (proyecto.getFreelancer().getNombre() + " " + proyecto.getFreelancer().getApellido())
                : null;

        return new ProyectoResponseDTO(
                proyecto.getId(),
                proyecto.getArea(),
                proyecto.getDescripcion(),
                proyecto.getFechaPublicacion(),
                proyecto.getPresupuestoMax(),
                proyecto.getPresupuestoMin(),
                clienteId,
                nombreCliente,
                proyecto.getDuracion(),
                proyecto.getTipoPago(),
                proyecto.getHabilidades() != null ? new java.util.ArrayList<>(proyecto.getHabilidades()) : new java.util.ArrayList<>(),
                estado,
                nombreFreelancer
        );
    }
}
