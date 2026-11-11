package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.CalificacionProyectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalificacionProyectoRepository extends JpaRepository<CalificacionProyectoEntity, Integer> {
    List<CalificacionProyectoEntity> findByProyecto_Id(Integer proyectoId);
    boolean existsByProyecto_Id(Integer proyectoId);

    @Query("SELECT cp FROM CalificacionProyectoEntity cp WHERE cp.proyecto.freelancer.employeeId = :employeeId")
    List<CalificacionProyectoEntity> findByFreelancerEmployeeId(@Param("employeeId") Integer employeeId);
}
