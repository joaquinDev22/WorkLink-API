package com.example.Proyecto_Final_Progra3.enterprise.talento;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    Optional<EmployeeEntity> findByPerfil(PerfilEntity perfil);
}

