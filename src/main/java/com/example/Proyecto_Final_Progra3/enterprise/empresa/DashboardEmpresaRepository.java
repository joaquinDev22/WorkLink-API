package com.example.Proyecto_Final_Progra3.enterprise.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardEmpresaRepository extends JpaRepository<DashboardEmpresaEntity, Integer> {
}
