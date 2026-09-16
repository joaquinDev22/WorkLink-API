package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

public interface ICalificacionBootcampService {
    CalificacionBootcampResponseDTO calificarBootcamp(Integer bootcampId, CalificacionBootcampRequestDTO dto);
    void validarCalificacion(CalificacionBootcampRequestDTO dto);
}
