package com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampInterfaces;

import com.example.Proyecto_Final_Progra3.dto.responsedto.InscripcionResponseDTO;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import java.util.List;

public interface IInscripcionService {
    InscripcionResponseDTO realizarInscripcion(Integer bootcampId, Integer employeeId);
    InscripcionResponseDTO realizarMiInscripcion(String username, Integer bootcampId);
    List<InscripcionResponseDTO> obtenerMisInscripciones(String username);
    InscripcionResponseDTO cancelarInscripcion(Integer inscripcionId);
    void validarInscripcionBootcamp(Bootcamp bootcamp, Employee employee);
}
