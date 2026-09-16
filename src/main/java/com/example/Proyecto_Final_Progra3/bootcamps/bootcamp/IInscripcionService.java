package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;
import java.util.List;

public interface IInscripcionService {
    InscripcionResponseDTO realizarInscripcion(Integer bootcampId, Integer employeeId);
    InscripcionResponseDTO realizarMiInscripcion(String username, Integer bootcampId);
    List<InscripcionResponseDTO> obtenerMisInscripciones(String username);
    InscripcionResponseDTO cancelarInscripcion(Integer inscripcionId);
    void validarInscripcionBootcamp(BootcampEntity bootcamp, EmployeeEntity employee);
}

