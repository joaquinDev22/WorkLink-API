package com.example.Proyecto_Final_Progra3.repository.enterprise;

import com.example.Proyecto_Final_Progra3.model.enterprise.PostulacionVacante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostulacionVacanteRepository extends JpaRepository<PostulacionVacante, Integer> {
    List<PostulacionVacante> findByVacante_VacanteId(Integer vacanteId);
    List<PostulacionVacante> findByEmployee_EmployeeId(Integer employeeId);
}
