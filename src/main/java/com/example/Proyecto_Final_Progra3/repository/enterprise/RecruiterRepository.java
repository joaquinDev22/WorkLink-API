package com.example.Proyecto_Final_Progra3.repository.enterprise;

import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.RecruiterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<RecruiterEntity, Integer> {
    Long countByEmpresa_Id(Integer empresaId);
    Optional<RecruiterEntity> findByPerfil(PerfilEntity perfil);
    java.util.List<RecruiterEntity> findByEmpresa_Id(Integer empresaId);
    Optional<RecruiterEntity> findByRecruiterIdAndEmpresa_Id(Integer recruiterId, Integer empresaId);
    boolean existsByEmail(String email);
    boolean existsByEmailAndRecruiterIdNot(String email, Integer recruiterId);
}

