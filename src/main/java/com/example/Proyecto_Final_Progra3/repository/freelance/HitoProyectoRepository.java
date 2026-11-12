package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.HitoProyectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HitoProyectoRepository extends JpaRepository<HitoProyectoEntity, Integer> {
    List<HitoProyectoEntity> findByProyecto_Id(Integer proyectoId);
    List<HitoProyectoEntity> findByProyecto_IdOrderByHitoIdAsc(Integer proyectoId);   // orden de creación = orden de trabajo (D5)
}
