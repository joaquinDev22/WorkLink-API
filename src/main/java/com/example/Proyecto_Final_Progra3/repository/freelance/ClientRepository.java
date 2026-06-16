package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    java.util.Optional<Client> findByPerfil(com.example.Proyecto_Final_Progra3.model.Perfil perfil);
}
