package com.example.Proyecto_Final_Progra3.enterprise.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntrevistaRepository extends JpaRepository<EntrevistaEntity, Integer> {
    
    @Query("SELECT e FROM EntrevistaEntity e WHERE e.postulacion.vacante.empresa.id = :empresaId")
    List<EntrevistaEntity> findByEmpresaId(@Param("empresaId") Integer empresaId);

    @Query("SELECT COUNT(e) FROM EntrevistaEntity e WHERE e.postulacion.vacante.empresa.id = :empresaId")
    Long countPendientesByEmpresaId(@Param("empresaId") Integer empresaId);
}
