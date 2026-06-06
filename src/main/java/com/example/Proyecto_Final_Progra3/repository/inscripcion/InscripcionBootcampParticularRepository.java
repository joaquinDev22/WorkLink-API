package com.example.Proyecto_Final_Progra3.repository.inscripcion;

import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.inscripciones.InscripcionBootcampParticular;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionBootcampParticularRepository extends JpaRepository <InscripcionBootcampParticular, Integer> {
    Boolean existsByBootcampParticularAndEmployee (Bootcamp bootcamp, Employee employee);

}
