package com.example.Proyecto_Final_Progra3.repository;

import com.example.Proyecto_Final_Progra3.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    Optional<Perfil> findByUsuario(String nombreUsuario);

    Integer id(int id);
}
