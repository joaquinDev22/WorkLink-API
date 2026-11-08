package com.example.Proyecto_Final_Progra3.service.employee.employeeInterfaces;

import com.example.Proyecto_Final_Progra3.dto.PerfilUsuarioDTO;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.employee.HabilidadEntity;

import java.util.List;

public interface IEmployeeService {
    List<HabilidadEntity> getAllHabilities();
    PerfilUsuarioDTO.HabilidadDTO addHabilidad(EmployeeEntity emp, String nombreHabilidad, Integer nivel);
    void removeHabilidad(EmployeeEntity emp, Integer habilidadId);
}
