package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoPostulacionProyecto;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.PostulacionProyectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostulacionProyectoRepository extends JpaRepository<PostulacionProyectoEntity, Integer> {
    List<PostulacionProyectoEntity> findByProyecto_Id(Integer proyectoId);
    long countByProyecto_Client_ClienteId(Integer clienteId);
    Optional<PostulacionProyectoEntity> findByProyecto_IdAndEmployee_EmployeeId(Integer proyectoId, Integer employeeId);
    Optional<PostulacionProyectoEntity> findByProyecto_IdAndEstado(Integer proyectoId, EstadoPostulacionProyecto estado);
    boolean existsByProyecto_IdAndEmployee_EmployeeId(Integer proyectoId, Integer employeeId);
}

