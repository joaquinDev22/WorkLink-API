package com.example.Proyecto_Final_Progra3.enterprise.talento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeHabilidadRepository extends JpaRepository<EmployeeHabilidadEntity, EmployeeHabilidadId> {
    List<EmployeeHabilidadEntity> findByEmployee(EmployeeEntity employee);
}

