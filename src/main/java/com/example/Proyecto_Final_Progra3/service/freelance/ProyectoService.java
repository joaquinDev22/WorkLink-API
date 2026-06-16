package com.example.Proyecto_Final_Progra3.service.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.ProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.ProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.Client;
import com.example.Proyecto_Final_Progra3.model.freelance.Proyecto;
import com.example.Proyecto_Final_Progra3.repository.freelance.ClientRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
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
        dto.setClienteId(proyecto.getClient().getClienteId());
        dto.setNombreCliente(proyecto.getClient().getFirstName() + " " + proyecto.getClient().getLastName());
        
        dto.setDuracion(proyecto.getDuracion());
        dto.setTipoPago(proyecto.getTipoPago());
        dto.setHabilidades(proyecto.getHabilidades() != null ? new java.util.ArrayList<>(proyecto.getHabilidades()) : new java.util.ArrayList<>());

        return dto;
    }
}
