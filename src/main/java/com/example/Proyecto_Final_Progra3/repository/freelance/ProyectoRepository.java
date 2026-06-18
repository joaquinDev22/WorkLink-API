package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    List<Proyecto> findByClient_ClienteId(Integer clientId);
}
