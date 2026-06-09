package com.example.Proyecto_Final_Progra3.service.freelance;

import com.example.Proyecto_Final_Progra3.model.Employee;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class FreelancerService {
    private final EmployeeRepository repository;
    public FreelancerService(EmployeeRepository repository) {
        this.repository = repository;
    }
}
