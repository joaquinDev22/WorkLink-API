package com.example.Proyecto_Final_Progra3.enterprise.empresa;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Integer> {
    Optional<EmpresaEntity> findByPerfil(PerfilEntity perfil);
    Optional<EmpresaEntity> findByPerfilUsuario(String usuario);
}

