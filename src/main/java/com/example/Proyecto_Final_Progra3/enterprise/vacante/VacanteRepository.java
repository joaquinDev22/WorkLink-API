package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacanteRepository extends JpaRepository<VacanteEntity, Integer> {
    List<VacanteEntity> findByEmpresa_Id(Integer empresaId);
    Long countByEmpresa_Id(Integer empresaId);
}

