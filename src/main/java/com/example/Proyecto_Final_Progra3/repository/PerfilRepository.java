package com.example.Proyecto_Final_Progra3.repository;

import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<PerfilEntity, Integer> {
    Optional<PerfilEntity> findByUsuario(String usuario);
    Optional<PerfilEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsuario(String usuario);
}

