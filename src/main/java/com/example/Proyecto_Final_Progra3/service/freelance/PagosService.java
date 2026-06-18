package com.example.Proyecto_Final_Progra3.service.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PagosRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.Proyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.pagos.Pagos;
import com.example.Proyecto_Final_Progra3.repository.freelance.PagosRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagosService {

    private final PagosRepository pagosRepository;
    private final ProyectoRepository proyectoRepository;

    @Transactional
    public PagosResponseDTO registrarPago(Integer proyectoId, PagosRequestDTO dto) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Pagos pago = new Pagos();
        pago.setMoneda(dto.getMoneda());
        pago.setMonto(dto.getMonto());
        pago.setMedioDePago(dto.getMedioDePago());
        pago.setProyecto(proyecto);

        Pagos guardado = pagosRepository.save(pago);
        return mapToDto(guardado);
    }

    @Transactional(readOnly = true)
    public List<PagosResponseDTO> listarPagos(Integer proyectoId) {
        return pagosRepository.findByProyecto_Id(proyectoId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PagosResponseDTO mapToDto(Pagos pago) {
        PagosResponseDTO dto = new PagosResponseDTO();
        dto.setPagoId(pago.getPagosId());
        dto.setMoneda(pago.getMoneda());
        dto.setMonto(pago.getMonto());
        dto.setMedioDePago(pago.getMedioDePago());
        dto.setProyectoId(pago.getProyecto().getId());
        return dto;
    }
}
