package com.example.Proyecto_Final_Progra3.repository.bootcamp;

import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<InstructorEntity, Integer> {
    Optional<InstructorEntity> findByPerfil(PerfilEntity perfil);
    Long countByEmpresa_Id(Integer empresaId);
}

