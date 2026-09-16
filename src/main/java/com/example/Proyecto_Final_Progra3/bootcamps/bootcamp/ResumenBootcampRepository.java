package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumenBootcampRepository extends JpaRepository<ResumenBootcampEntity, Integer> {
}
