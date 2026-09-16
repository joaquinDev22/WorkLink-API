package com.example.Proyecto_Final_Progra3.freelance.proyecto;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoPerfilRepository extends JpaRepository<ProyectoPerfilEntity, Integer> {
    List<ProyectoPerfilEntity> findByPerfilOrderByFechaCreacionDesc(PerfilEntity perfil);
    long countByPerfil(PerfilEntity perfil);
}

