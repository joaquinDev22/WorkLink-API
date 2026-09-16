package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostulacionVacanteRepository extends JpaRepository<PostulacionVacanteEntity, Integer> {
    List<PostulacionVacanteEntity> findByVacante_VacanteId(Integer vacanteId);
    List<PostulacionVacanteEntity> findByEmployee_EmployeeId(Integer employeeId);

    @Procedure(procedureName = "sp_postular_vacante")
    String postularVacante(
            @Param("p_vacante_id") Integer vacanteId,
            @Param("p_postulante_id") Integer postulanteId,
            @Param("p_cv") byte[] cv,
            @Param("p_mensaje") String mensaje
    );
}
