package com.example.Proyecto_Final_Progra3.repository.views;

import com.example.Proyecto_Final_Progra3.model.views.ResumenBootcampEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumenBootcampRepository extends JpaRepository<ResumenBootcampEntity, Integer> {
}
