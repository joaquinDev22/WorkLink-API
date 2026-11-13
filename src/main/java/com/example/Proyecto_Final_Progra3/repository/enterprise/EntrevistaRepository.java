package com.example.Proyecto_Final_Progra3.repository.enterprise;

import com.example.Proyecto_Final_Progra3.model.enterprise.EntrevistaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrevistaRepository extends JpaRepository<EntrevistaEntity, Integer> {
    
    @Query("SELECT e FROM EntrevistaEntity e WHERE e.postulacion.vacante.empresa.id = :empresaId")
    List<EntrevistaEntity> findByEmpresaId(@Param("empresaId") Integer empresaId);

    @Query("SELECT e FROM EntrevistaEntity e WHERE e.postulacion.employee.perfil.usuario = :username")
    List<EntrevistaEntity> findByPostulanteUsuario(@Param("username") String username);

    @Query("SELECT COUNT(e) FROM EntrevistaEntity e WHERE e.postulacion.vacante.empresa.id = :empresaId")
    Long countPendientesByEmpresaId(@Param("empresaId") Integer empresaId);

    List<EntrevistaEntity> findByPostulacion_PostulacionVacanteId(Integer postulacionId);
}
