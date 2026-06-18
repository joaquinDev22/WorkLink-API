package com.example.Proyecto_Final_Progra3.repository.employee;

import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeHabilidad;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeHabilidadId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeHabilidadRepository extends JpaRepository<EmployeeHabilidad, EmployeeHabilidadId> {
    List<EmployeeHabilidad> findByEmployee(Employee employee);
}
