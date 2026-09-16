package com.example.Proyecto_Final_Progra3.auth.perfil;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {
    RolEntity findByNombre(String rol);
}

