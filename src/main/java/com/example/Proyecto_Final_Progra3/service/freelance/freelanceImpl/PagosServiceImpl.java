package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PagosRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.Proyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.pagos.Pagos;
import com.example.Proyecto_Final_Progra3.repository.freelance.PagosRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ProyectoRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IPagosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagosServiceImpl implements IPagosService {

    private final PagosRepository pagosRepository;
    private final ProyectoRepository proyectoRepository;

    @Override
    @Transactional
    public PagosResponseDTO registrarPago(Integer proyectoId, PagosRequestDTO dto) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Pagos pago = new Pagos();
        pago.setMoneda(dto.moneda());
        pago.setMonto(dto.monto());
        pago.setMedioDePago(dto.medioDePago());
        pago.setProyecto(proyecto);

        Pagos guardado = pagosRepository.save(pago);
        return mapToDto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagosResponseDTO> listarPagos(Integer proyectoId) {
        return pagosRepository.findByProyecto_Id(proyectoId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PagosResponseDTO mapToDto(Pagos pago) {
        return new PagosResponseDTO(
                pago.getPagosId(),
                pago.getMoneda(),
                pago.getMonto(),
                pago.getMedioDePago(),
                pago.getProyecto().getId()
        );
    }
}
