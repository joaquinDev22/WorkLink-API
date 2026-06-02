package com.example.Proyecto_Final_Progra3.service.freelance;

import com.example.Proyecto_Final_Progra3.repository.freelance.FreelancerRepository;
import org.springframework.stereotype.Service;

@Service
public class FreelancerService {
    private final FreelancerRepository repository;
    public FreelancerService(FreelancerRepository repository) {
        this.repository = repository;
    }
}
