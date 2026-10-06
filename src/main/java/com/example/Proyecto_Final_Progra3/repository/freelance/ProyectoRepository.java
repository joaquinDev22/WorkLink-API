package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<ProyectoEntity, Integer> {
    List<ProyectoEntity> findByClient_ClienteId(Integer clientId);
    List<ProyectoEntity> findByFreelancer_EmployeeId(Integer employeeId);

    long countByClient_ClienteIdAndEstadoIn(Integer clientId, List<EstadoProyecto> estados);
    long countByClient_ClienteIdAndEstado(Integer clientId, EstadoProyecto estado);

    long countByFreelancer_EmployeeIdAndEstadoIn(Integer employeeId, List<EstadoProyecto> estados);
    long countByFreelancer_EmployeeIdAndEstado(Integer employeeId, EstadoProyecto estado);

    @Query("SELECT COALESCE(SUM(p.presupuestoMax), 0) FROM ProyectoEntity p WHERE p.freelancer.employeeId = :employeeId AND p.estado IN :estados")
    Double sumPresupuestoByFreelancerAndEstados(@Param("employeeId") Integer employeeId, @Param("estados") List<EstadoProyecto> estados);
}

