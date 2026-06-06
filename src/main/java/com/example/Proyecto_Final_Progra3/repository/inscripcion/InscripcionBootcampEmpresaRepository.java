package com.example.Proyecto_Final_Progra3.repository.inscripcion;

import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEmpresa;
import com.example.Proyecto_Final_Progra3.model.bootcamps.inscripcion.InscripcionBootcampEmpresa;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionBootcampEmpresaRepository extends JpaRepository<InscripcionBootcampEmpresa, Integer> {
    Boolean existsByBootcampEmpresaAndEmployee (Bootcamp bootcamp, Employee employee);
}
