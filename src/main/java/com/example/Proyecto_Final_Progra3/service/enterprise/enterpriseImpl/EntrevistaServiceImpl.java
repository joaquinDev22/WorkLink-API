package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.responsedto.EntrevistaResponseDTO;
import com.example.Proyecto_Final_Progra3.mappers.EntrevistaMapper;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EntrevistaRepository;
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
    private final EntrevistaMapper entrevistaMapper;

    @Override
    public List<EntrevistaResponseDTO> listarEntrevistas() {
        return entrevistaMapper.toDtoList(entrevistaRepository.findAll());
    }
}
