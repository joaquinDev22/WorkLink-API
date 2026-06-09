package com.example.Proyecto_Final_Progra3.repository;

import com.example.Proyecto_Final_Progra3.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
