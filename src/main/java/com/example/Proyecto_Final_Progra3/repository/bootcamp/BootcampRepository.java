package com.example.Proyecto_Final_Progra3.repository.bootcamp;

import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BootcampRepository extends JpaRepository<BootcampEntity, Integer> {
    List<BootcampEntity> findByCuposGreaterThanAndInicioGreaterThanEqual(int cupos, LocalDate fecha);

    List<BootcampEntity> findByModalidad(String modalidad);

    List<BootcampEntity> findByInstructor(InstructorEntity instructor);

    Long countByEmpresa_Id(Integer empresaId);
}

