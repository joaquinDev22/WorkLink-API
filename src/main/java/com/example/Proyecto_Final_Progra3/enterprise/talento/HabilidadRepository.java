package com.example.Proyecto_Final_Progra3.enterprise.talento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabilidadRepository extends JpaRepository<HabilidadEntity, Integer> {
    Optional<HabilidadEntity> findByNombreHabilidad(String nombre);
}

