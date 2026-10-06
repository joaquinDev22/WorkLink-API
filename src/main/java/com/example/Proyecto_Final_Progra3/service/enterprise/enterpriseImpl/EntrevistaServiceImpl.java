package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.EntrevistaRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.EntrevistaResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.mappers.EntrevistaMapper;
import com.example.Proyecto_Final_Progra3.model.enterprise.EntrevistaEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.PostulacionVacanteEntity;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EntrevistaRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.PostulacionVacanteRepository;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IEntrevistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EntrevistaServiceImpl implements IEntrevistaService {

    private final EntrevistaRepository entrevistaRepository;
    private final PostulacionVacanteRepository postulacionVacanteRepository;
    private final EntrevistaMapper entrevistaMapper;

    @Override
    public List<EntrevistaResponseDTO> listarEntrevistas() {
        return entrevistaMapper.toDtoList(entrevistaRepository.findAll());
    }

    @Override
    @Transactional
    public EntrevistaResponseDTO agendarEntrevista(EntrevistaRequestDTO dto) {
        PostulacionVacanteEntity postulacion = postulacionVacanteRepository.findById(dto.postulacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada con ID: " + dto.postulacionId()));

        EntrevistaEntity entrevista = EntrevistaEntity.builder()
                .postulacion(postulacion)
                .fecha(dto.fecha())
                .hora(dto.hora())
                .link(dto.link())
                .build();

        EntrevistaEntity guardada = entrevistaRepository.save(entrevista);
        return entrevistaMapper.toDto(guardada);
    }
}
