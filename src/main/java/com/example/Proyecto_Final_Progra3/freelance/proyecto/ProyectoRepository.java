package com.example.Proyecto_Final_Progra3.freelance.proyecto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<ProyectoEntity, Integer> {
    List<ProyectoEntity> findByClient_ClienteId(Integer clientId);
    List<ProyectoEntity> findByFreelancer_EmployeeId(Integer employeeId);
}

