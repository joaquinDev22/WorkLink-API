package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.PostulacionProyectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostulacionProyectoRepository extends JpaRepository<PostulacionProyectoEntity, Integer> {
    List<PostulacionProyectoEntity> findByProyecto_Id(Integer proyectoId);
    long countByProyecto_Client_ClienteId(Integer clienteId);
}

