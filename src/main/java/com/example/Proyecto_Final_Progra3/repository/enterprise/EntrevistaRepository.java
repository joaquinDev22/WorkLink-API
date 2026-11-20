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

    @Query("SELECT COUNT(e) FROM EntrevistaEntity e WHERE e.postulacion.vacante.empresa.id = :empresaId "
            + "AND e.status <> com.example.Proyecto_Final_Progra3.model.enterprise.EstadoEntrevista.CANCELADA")
    Long countPendientesByEmpresaId(@Param("empresaId") Integer empresaId);

    List<EntrevistaEntity> findByPostulacion_PostulacionVacanteId(Integer postulacionId);

    // Otra entrevista no cancelada de la misma empresa en ese día y horario (sin contar la que se reprograma)
    @Query("SELECT COUNT(e) > 0 FROM EntrevistaEntity e WHERE e.postulacion.vacante.empresa.id = :empresaId "
            + "AND e.fecha = :fecha AND e.hora = :hora "
            + "AND e.status <> com.example.Proyecto_Final_Progra3.model.enterprise.EstadoEntrevista.CANCELADA "
            + "AND (:excluirId IS NULL OR e.id <> :excluirId)")
    boolean existeOtraEnHorario(@Param("empresaId") Integer empresaId, @Param("fecha") java.time.LocalDate fecha,
                                @Param("hora") java.time.LocalTime hora, @Param("excluirId") Integer excluirId);
}
