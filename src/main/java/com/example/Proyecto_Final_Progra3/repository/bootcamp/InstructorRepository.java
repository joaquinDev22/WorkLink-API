package com.example.Proyecto_Final_Progra3.repository.bootcamp;

import com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import com.example.Proyecto_Final_Progra3.model.Perfil;

public interface InstructorRepository extends JpaRepository<Instructor,Integer> {
    Optional<Instructor> findByPerfil(Perfil perfil);
    Long countByEmpresa_Id(Integer empresaId);
}
