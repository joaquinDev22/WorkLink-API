package com.example.Proyecto_Final_Progra3.freelance.pagos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagosRepository extends JpaRepository<PagosEntity, Integer> {
    List<PagosEntity> findByProyecto_Id(Integer proyectoId);
}

