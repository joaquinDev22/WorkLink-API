package com.example.Proyecto_Final_Progra3.repository.bootcamp;

import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEmpresa;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampParticular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BootcampParticularRepository extends JpaRepository<BootcampParticular, Integer> {
    List<BootcampParticular> findByCuposGreaterThanAndFechaInicioGreaterThanEqual(Integer cupos, LocalDate fechaInicio);
    List<BootcampParticular> findByModalidad(String modalidad);

}
