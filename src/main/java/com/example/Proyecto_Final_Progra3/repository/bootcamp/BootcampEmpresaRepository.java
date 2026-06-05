package com.example.Proyecto_Final_Progra3.repository.bootcamp;

import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BootcampEmpresaRepository extends JpaRepository<BootcampEmpresa, Integer> {
    List<BootcampEmpresa> findByCuposGreaterThan(Integer cupos);

    List<BootcampEmpresa> findByCuposGreaterThanAndFechaInicioGreaterThanEqual(Integer cupos, LocalDate fechaInicio);

    List<BootcampEmpresa> findByEmpresa_Id(int empresaId);

    List<BootcampEmpresa> findByModalidad(String modalidad);
}