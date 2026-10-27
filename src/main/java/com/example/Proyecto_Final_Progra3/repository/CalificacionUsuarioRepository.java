package com.example.Proyecto_Final_Progra3.repository;

import com.example.Proyecto_Final_Progra3.model.CalificacionUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalificacionUsuarioRepository extends JpaRepository<CalificacionUsuarioEntity, Integer> {
    List<CalificacionUsuarioEntity> findByPerfil_Id(Integer perfilId);
}

