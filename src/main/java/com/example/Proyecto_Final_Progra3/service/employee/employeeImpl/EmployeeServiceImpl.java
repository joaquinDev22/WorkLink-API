package com.example.Proyecto_Final_Progra3.service.employee.employeeImpl;

import com.example.Proyecto_Final_Progra3.model.employee.Habilidad;
import com.example.Proyecto_Final_Progra3.repository.employee.HabilidadRepository;
import com.example.Proyecto_Final_Progra3.service.employee.employeeInterfaces.IEmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements IEmployeeService {
    private final HabilidadRepository repository;

    @Override
    public List<Habilidad> getAllHabilities(){
        return repository.findAll();
    }
}
