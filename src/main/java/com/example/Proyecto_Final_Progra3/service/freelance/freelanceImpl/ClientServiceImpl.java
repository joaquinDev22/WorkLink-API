package com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl;

import com.example.Proyecto_Final_Progra3.repository.freelance.ClientRepository;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces.IClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClientServiceImpl implements IClientService {
    private final ClientRepository repository;
}
