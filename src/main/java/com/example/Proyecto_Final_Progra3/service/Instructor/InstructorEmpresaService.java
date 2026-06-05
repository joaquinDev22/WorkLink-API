package com.example.Proyecto_Final_Progra3.service.Instructor;

import com.example.Proyecto_Final_Progra3.dto.requestdto.InstructorEmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.exceptions.InstructorInvalidoException;
import com.example.Proyecto_Final_Progra3.model.Instructor.InstructorEmpresa;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.instructor.InstructorEmpresaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class InstructorEmpresaService {
    private final InstructorEmpresaRepository instructorEmpresaRepository;
    private final EmpresaRepository empresaRepository;

    public InstructorEmpresa crearInstructorEmpresa(InstructorEmpresaRequestDTO dto) {
        validarInstructorEmpresa(dto);
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("No existe la empresa"));

        InstructorEmpresa instructor = new InstructorEmpresa();
        instructor.setNombre(dto.getNombre());
        instructor.setTitulo(dto.getTitulo());
        instructor.setDescripcion(dto.getDescripcion());
        instructor.setEmpresa(empresa);
        return instructorEmpresaRepository.save(instructor);
    }

    public void eliminarInstructorEmpresa(Integer idInstructor) {
        InstructorEmpresa instructorEmpresa = instructorEmpresaRepository.findById(idInstructor)
                .orElseThrow(() -> new InstructorInvalidoException("No existe un instructor con ese id"));
        instructorEmpresaRepository.delete(instructorEmpresa);
    }
    

    private void validarInstructorEmpresa(InstructorEmpresaRequestDTO dto) {

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

        if (dto.getEmpresaId() == null) {
            throw new RuntimeException("La empresa es obligatoria");
        }
    }

}
