package com.example.Proyecto_Final_Progra3.repository.bootcamp;

import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BootcampRepository extends JpaRepository<Bootcamp, Integer> {
    List<Bootcamp> findByCuposGreaterThanAndInicioGreaterThanEqual(int cupos, LocalDate fecha);
    List<Bootcamp> findByModalidad(String modalidad);
}
