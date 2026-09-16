package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CalificacionBootcampRepository extends JpaRepository<CalificacionBootcampEntity, Integer> {
    boolean existsByBootcampAndEmployee(BootcampEntity bootcamp, EmployeeEntity employee);
}

