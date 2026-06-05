package com.example.Proyecto_Final_Progra3.service.Instructor;

import com.example.Proyecto_Final_Progra3.dto.requestdto.InstructorParticularRequestDTO;
import com.example.Proyecto_Final_Progra3.model.Instructor.InstructorParticular;
import com.example.Proyecto_Final_Progra3.repository.instructor.InstructorParticularRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class InstructorParticularService {
    private final InstructorParticularRepository instructorParticularRepository;

    public InstructorParticular crearInstructorParticular(InstructorParticularRequestDTO dto) {

        validarInstructorParticular(dto);

        InstructorParticular instructor = new InstructorParticular();
        instructor.setNombre(dto.getNombre());
        instructor.setTitulo(dto.getTitulo());
        instructor.setDescripcion(dto.getDescripcion());

        return instructorParticularRepository.save(instructor);
    }

    public List<InstructorParticular> listarInstructoresParticulares() {
        return instructorParticularRepository.findAll();
    }

    public InstructorParticular buscarInstructorParticularPorId(Integer id) {
        return instructorParticularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un instructor particular con ese ID"));
    }

    public InstructorParticular modificarInstructorParticular(Integer id, InstructorParticularRequestDTO dto) {

        InstructorParticular instructor = instructorParticularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un instructor particular con ese ID"));

        validarInstructorParticular(dto);

        instructor.setNombre(dto.getNombre());
        instructor.setTitulo(dto.getTitulo());
        instructor.setDescripcion(dto.getDescripcion());

        return instructorParticularRepository.save(instructor);
    }

    public void eliminarInstructorParticular(Integer id) {

        InstructorParticular instructor = instructorParticularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un instructor particular con ese ID"));

        instructorParticularRepository.delete(instructor);
    }

    private void validarInstructorParticular(InstructorParticularRequestDTO dto) {

        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del instructor es obligatorio");
        }

        if (dto.getNombre().length() > 100) {
            throw new RuntimeException("El nombre no puede superar los 100 caracteres");
        }

        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new RuntimeException("El título del instructor es obligatorio");
        }

        if (dto.getTitulo().length() > 100) {
            throw new RuntimeException("El título no puede superar los 100 caracteres");
        }

        if (dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
            throw new RuntimeException("La descripción del instructor es obligatoria");
        }

        if (dto.getDescripcion().length() > 255) {
            throw new RuntimeException("La descripción no puede superar los 255 caracteres");
        }
    }
}

