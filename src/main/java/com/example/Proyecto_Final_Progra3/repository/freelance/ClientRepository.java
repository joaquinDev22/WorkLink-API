package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
    @Query("SELECT c FROM ClientEntity c LEFT JOIN FETCH c.perfil WHERE c.perfil = :perfil")
    Optional<ClientEntity> findByPerfil(@Param("perfil") PerfilEntity perfil);

    Optional<ClientEntity> findByEmail(String email);
}

