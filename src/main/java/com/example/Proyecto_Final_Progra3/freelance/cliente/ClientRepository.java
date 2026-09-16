package com.example.Proyecto_Final_Progra3.freelance.cliente;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
    Optional<ClientEntity> findByPerfil(PerfilEntity perfil);
}

