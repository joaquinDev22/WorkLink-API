package com.example.Proyecto_Final_Progra3.repository.enterprise;

import com.example.Proyecto_Final_Progra3.model.enterprise.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {
    Long countByEmpresa_Id(Integer empresaId);
    java.util.Optional<Recruiter> findByPerfil(com.example.Proyecto_Final_Progra3.model.Perfil perfil);
}
