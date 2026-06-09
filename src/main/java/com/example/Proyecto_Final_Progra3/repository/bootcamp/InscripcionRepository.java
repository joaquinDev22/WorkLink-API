package com.example.Proyecto_Final_Progra3.repository.bootcamp;

import com.example.Proyecto_Final_Progra3.model.Employee;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion,Integer> {
    boolean existsByBootcampAndEmployee(Bootcamp bootcamp, Employee employee);
}
