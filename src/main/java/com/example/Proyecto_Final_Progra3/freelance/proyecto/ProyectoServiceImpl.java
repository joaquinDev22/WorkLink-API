package com.example.Proyecto_Final_Progra3.freelance.proyecto;

import com.example.Proyecto_Final_Progra3.errors.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.errors.UnauthorizedAccessException;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;
import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.freelance.cliente.ClientEntity;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilRepository;
import com.example.Proyecto_Final_Progra3.freelance.cliente.ClientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProyectoServiceImpl implements IProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final ClientRepository clientRepository;
    private final PerfilRepository perfilRepository;
    private final EmployeeRepository employeeRepository;
    private final ProyectoMapper proyectoMapper;

    @Override
    @Transactional
    public ProyectoResponseDTO crearProyecto(ProyectoRequestDTO dto) {
        ClientEntity client = clientRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + dto.clienteId()));

        ProyectoEntity proyecto = new ProyectoEntity();
        proyecto.setArea(dto.area());
        proyecto.setDescripcion(dto.descripcion());
        proyecto.setFechaPublicacion(LocalDate.now());
        proyecto.setPresupuestoMax(dto.presupuestoMax());
        proyecto.setPresupuestoMin(dto.presupuestoMin());
        proyecto.setClient(client);

        proyecto.setDuracion(dto.duracion());
        proyecto.setTipoPago(dto.tipoPago());
        proyecto.setHabilidades(dto.habilidades());

        ProyectoEntity guardado = proyectoRepository.save(proyecto);
        return proyectoMapper.toDto(guardado);
    }

    @Override
    @Transactional
    public ProyectoResponseDTO crearMiProyecto(String username, ProyectoRequestDTO dto) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        ClientEntity client = clientRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("Solo los clientes pueden crear proyectos"));

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
        ProyectoEntity proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));

        if (!proyecto.getClient().getPerfil().getUsuario().equals(username)) {
            throw new UnauthorizedAccessException("No tienes permiso para cancelar este proyecto");
        }

        if (proyecto.getEstado() != EstadoProyecto.BUSCANDO_TALENTO) {
            throw new BusinessRuleException("Solo puedes cancelar proyectos que estén buscando talento");
        }

        proyecto.setEstado(EstadoProyecto.CANCELADO);
        proyectoRepository.save(proyecto);
    }

    @Override
    public ProyectoResponseDTO buscarPorId(Integer id) {
        return proyectoRepository.findById(id)
                .map(proyectoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));
    }

    @Override
    public List<ProyectoResponseDTO> listarTodos() {
        return proyectoMapper.toDtoList(proyectoRepository.findAll());
    }

    @Override
    public List<ProyectoResponseDTO> listarMisProyectosPublicados(String username) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        ClientEntity client = clientRepository.findByPerfil(perfil)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado para el usuario: " + username));

        return proyectoMapper.toDtoList(proyectoRepository.findByClient_ClienteId(client.getClienteId()));
    }

    @Override
    public List<ProyectoResponseDTO> listarMisProyectosAsignados(String username) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        EmployeeEntity employee = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer no encontrado para el usuario: " + username));

        return proyectoMapper.toDtoList(proyectoRepository.findByFreelancer_EmployeeId(employee.getEmployeeId()));
    }
}
