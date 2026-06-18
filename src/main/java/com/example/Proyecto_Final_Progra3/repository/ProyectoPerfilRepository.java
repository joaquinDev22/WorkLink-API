package com.example.Proyecto_Final_Progra3.repository;

import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.model.ProyectoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoPerfilRepository extends JpaRepository<ProyectoPerfil, Integer> {
    List<ProyectoPerfil> findByPerfilOrderByFechaCreacionDesc(Perfil perfil);
    long countByPerfil(Perfil perfil);
}
