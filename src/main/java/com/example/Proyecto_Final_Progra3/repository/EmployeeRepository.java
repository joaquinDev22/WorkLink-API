package com.example.Proyecto_Final_Progra3.repository;

import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    java.util.Optional<Employee> findByPerfil(com.example.Proyecto_Final_Progra3.model.Perfil perfil);
}
