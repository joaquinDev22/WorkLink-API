package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.PostulacionProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostulacionProyectoRepository extends JpaRepository<PostulacionProyecto, Integer> {
    List<PostulacionProyecto> findByProyecto_Id(Integer proyectoId);
}
