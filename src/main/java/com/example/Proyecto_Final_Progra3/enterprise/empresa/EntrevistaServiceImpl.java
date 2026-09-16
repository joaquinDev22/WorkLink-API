package com.example.Proyecto_Final_Progra3.enterprise.empresa;

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
