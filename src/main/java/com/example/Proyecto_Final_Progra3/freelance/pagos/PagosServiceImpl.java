package com.example.Proyecto_Final_Progra3.freelance.pagos;

import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;

import com.example.Proyecto_Final_Progra3.freelance.proyecto.ProyectoEntity;

import com.example.Proyecto_Final_Progra3.freelance.proyecto.ProyectoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PagosServiceImpl implements IPagosService {

    private final PagosRepository pagosRepository;
    private final ProyectoRepository proyectoRepository;
    private final PagosMapper pagosMapper;

    @Override
    @Transactional
    public PagosResponseDTO registrarPago(Integer proyectoId, PagosRequestDTO dto) {
        ProyectoEntity proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        PagosEntity pago = new PagosEntity();
        pago.setMoneda(dto.moneda());
        pago.setMonto(dto.monto());
        pago.setMedioDePago(dto.medioDePago());
        pago.setProyecto(proyecto);

        PagosEntity guardado = pagosRepository.save(pago);
        return pagosMapper.toDto(guardado);
    }

    @Override
    public List<PagosResponseDTO> listarPagos(Integer proyectoId) {
        return pagosMapper.toDtoList(pagosRepository.findByProyecto_Id(proyectoId));
    }
}
