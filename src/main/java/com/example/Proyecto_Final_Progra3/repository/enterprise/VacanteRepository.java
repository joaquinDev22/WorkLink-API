package com.example.Proyecto_Final_Progra3.repository.enterprise;

import com.example.Proyecto_Final_Progra3.model.enterprise.VacanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VacanteRepository extends JpaRepository<VacanteEntity, Integer> {
    Optional<VacanteEntity> findByUuid(UUID uuid);
    List<VacanteEntity> findByEmpresa_Id(Integer empresaId);
    Long countByEmpresa_Id(Integer empresaId);
}
