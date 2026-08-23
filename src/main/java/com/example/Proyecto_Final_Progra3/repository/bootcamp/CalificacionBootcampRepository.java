package com.example.Proyecto_Final_Progra3.repository.bootcamp;

import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.CalificacionBootcampEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalificacionBootcampRepository extends JpaRepository<CalificacionBootcampEntity, Integer> {
    boolean existsByBootcampAndEmployee(BootcampEntity bootcamp, EmployeeEntity employee);
}

