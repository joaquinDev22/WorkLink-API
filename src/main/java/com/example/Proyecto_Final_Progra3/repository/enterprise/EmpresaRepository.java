package com.example.Proyecto_Final_Progra3.repository.enterprise;

import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Integer> {
    Optional<EmpresaEntity> findByUuid(UUID uuid);
    Optional<EmpresaEntity> findByPerfil(PerfilEntity perfil);
    Optional<EmpresaEntity> findByPerfilUsuario(String usuario);
}
