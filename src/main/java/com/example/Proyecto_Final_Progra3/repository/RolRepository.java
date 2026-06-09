package com.example.Proyecto_Final_Progra3.repository;

import com.example.Proyecto_Final_Progra3.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Rol findByNombre(String rol);
}
