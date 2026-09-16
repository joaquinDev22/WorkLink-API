package com.example.Proyecto_Final_Progra3.enterprise.empresa;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<RecruiterEntity, Integer> {
    Long countByEmpresa_Id(Integer empresaId);
    Optional<RecruiterEntity> findByPerfil(PerfilEntity perfil);
}

