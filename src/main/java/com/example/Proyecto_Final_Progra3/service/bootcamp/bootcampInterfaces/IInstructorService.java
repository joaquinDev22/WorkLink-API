package com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.InstructorRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.InstructorResponseDTO;

import java.util.List;

public interface IInstructorService {
    public InstructorResponseDTO crearInstructor(InstructorRequestDTO dto);
    void eliminarInstructor(Integer instructorId);
    InstructorResponseDTO modificarInstructor(Integer instructorId, InstructorRequestDTO dto);
    List<InstructorResponseDTO> listarInstuctores();
    void validarInstructor(InstructorRequestDTO dto);
}
