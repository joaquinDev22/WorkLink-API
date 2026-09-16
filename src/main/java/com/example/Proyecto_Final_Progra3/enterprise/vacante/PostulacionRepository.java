package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostulacionRepository extends JpaRepository<PostulacionVacanteEntity, Integer> {
    Long countByVacante_Empresa_Id(Integer vacante_Empresa_Id);
}

