package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.pagos.PagosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagosRepository extends JpaRepository<PagosEntity, Integer> {
    List<PagosEntity> findByProyecto_Id(Integer proyectoId);
    List<PagosEntity> findByProyecto_Client_ClienteId(Integer clienteId);
    List<PagosEntity> findByProyecto_Freelancer_EmployeeId(Integer employeeId);

    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM PagosEntity p WHERE p.proyecto.client.clienteId = :clienteId")
    Double sumMontoByClienteId(@Param("clienteId") Integer clienteId);

    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM PagosEntity p WHERE p.proyecto.freelancer.employeeId = :employeeId")
    Double sumMontoByFreelancerId(@Param("employeeId") Integer employeeId);
}

