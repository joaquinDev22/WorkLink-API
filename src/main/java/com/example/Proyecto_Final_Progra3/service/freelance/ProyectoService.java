package com.example.Proyecto_Final_Progra3.service.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.ProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.Client;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.Proyecto;
import com.example.Proyecto_Final_Progra3.repository.freelance.ClientRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final ClientRepository clientRepository;
    private final PerfilRepository perfilRepository;

    @Transactional
    public ProyectoResponseDTO crearProyecto(ProyectoRequestDTO dto) {
        Client client = clientRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Proyecto proyecto = new Proyecto();
        proyecto.setArea(dto.getArea());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setFechaPublicacion(LocalDate.now());
        proyecto.setPresupuestoMax(dto.getPresupuestoMax());
        proyecto.setPresupuestoMin(dto.getPresupuestoMin());
        proyecto.setClient(client);
        
        proyecto.setDuracion(dto.getDuracion());
        proyecto.setTipoPago(dto.getTipoPago());
        proyecto.setHabilidades(dto.getHabilidades());

        Proyecto guardado = proyectoRepository.save(proyecto);
        return mapToDto(guardado);
    }

    @Transactional
    public ProyectoResponseDTO crearMiProyecto(String username, ProyectoRequestDTO dto) {
        com.example.Proyecto_Final_Progra3.model.Perfil perfil = perfilRepository
                .findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        Client client = clientRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo los clientes pueden crear proyectos"));
                
        dto.setClienteId(client.getClienteId());
        return crearProyecto(dto);
    }

    @Transactional(readOnly = true)
    public ProyectoResponseDTO buscarPorId(Integer id) {
        return proyectoRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<ProyectoResponseDTO> listarTodos() {
        return proyectoRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ProyectoResponseDTO mapToDto(Proyecto proyecto) {
        ProyectoResponseDTO dto = new ProyectoResponseDTO();
        dto.setProyectoId(proyecto.getId());
        dto.setArea(proyecto.getArea());
        dto.setDescripcion(proyecto.getDescripcion());
        dto.setFechaPublicacion(proyecto.getFechaPublicacion());
        dto.setPresupuestoMax(proyecto.getPresupuestoMax());
        dto.setPresupuestoMin(proyecto.getPresupuestoMin());
        if (proyecto.getClient() != null) {
            dto.setClienteId(proyecto.getClient().getClienteId());
            dto.setNombreCliente(proyecto.getClient().getFirstName() + " " + proyecto.getClient().getLastName());
        } else {
            dto.setNombreCliente("Cliente Desconocido");
        }
        
        dto.setDuracion(proyecto.getDuracion());
        dto.setTipoPago(proyecto.getTipoPago());
        dto.setHabilidades(proyecto.getHabilidades() != null ? new java.util.ArrayList<>(proyecto.getHabilidades()) : new java.util.ArrayList<>());

        return dto;
    }
}
