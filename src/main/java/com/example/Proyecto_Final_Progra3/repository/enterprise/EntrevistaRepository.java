package com.example.Proyecto_Final_Progra3.repository.enterprise;

import com.example.Proyecto_Final_Progra3.model.enterprise.Entrevista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntrevistaRepository extends JpaRepository<Entrevista, Integer> {
    
    @Query("SELECT e FROM Entrevista e WHERE e.postulacion.vacante.empresa.id = :empresaId")
    List<Entrevista> findByEmpresaId(@Param("empresaId") Integer empresaId);
}
