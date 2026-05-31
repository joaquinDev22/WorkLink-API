package com.example.Proyecto_Final_Progra3.repository.enterprise;

import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    List<Empresa> buscarEmpresaXidPerfil(int idPerfil);
    List<Empresa> findByPerfil(Perfil perfil);
}
