package com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.CalificacionBootcampRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.CalificacionBootcampResponseDTO;

public interface ICalificacionBootcampService {
    CalificacionBootcampResponseDTO calificarBootcamp(Integer bootcampId, CalificacionBootcampRequestDTO dto);
    void validarCalificacion(CalificacionBootcampRequestDTO dto);
}
