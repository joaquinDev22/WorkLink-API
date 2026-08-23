package com.example.Proyecto_Final_Progra3.repository.views;

import com.example.Proyecto_Final_Progra3.model.views.DashboardEmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardEmpresaRepository extends JpaRepository<DashboardEmpresaEntity, Integer> {
}
