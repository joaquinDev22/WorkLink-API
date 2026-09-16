package com.example.Proyecto_Final_Progra3.bootcamps.instructor;

import java.util.List;

public interface IInstructorService {
    public InstructorResponseDTO crearInstructor(InstructorRequestDTO dto);
    void eliminarInstructor(Integer instructorId);
    InstructorResponseDTO modificarInstructor(Integer instructorId, InstructorRequestDTO dto);
    List<InstructorResponseDTO> listarInstuctores();
    void validarInstructor(InstructorRequestDTO dto);
}
