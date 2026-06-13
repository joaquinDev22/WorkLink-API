package com.example.Proyecto_Final_Progra3.service.bootcamp;


import com.example.Proyecto_Final_Progra3.dto.requestdto.InstructorRequestDTO;
import com.example.Proyecto_Final_Progra3.exceptions.bootcamps.InstructorInvalidoException;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class InstructorService {
    private final InstructorRepository repository;
    private final EmpresaRepository empresaRepository;

    public Instructor crearInstructor(InstructorRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresa_id())
                .orElseThrow(() -> new RuntimeException("No existe la empresa"));
        Instructor instructor = new Instructor();

        validarInstructor(dto);

        instructor.setNombre(dto.getNombre());
        instructor.setTitulo(dto.getTitulo());
        instructor.setPresentacion(dto.getPresentacion());

        if (dto.getTipo().equals(Tipo.EMPRESA)) {
            instructor.setEmpresa(empresa);
        }

        return repository.save(instructor);
    }

    public void eliminarInstructor(Integer instructorId) {
        Instructor instructorEmpresa = repository.findById(instructorId)
                .orElseThrow(() -> new InstructorInvalidoException("No existe un instructor con ese id"));
        repository.delete(instructorEmpresa);
    }

    public Instructor modificarInstructor(Integer instructorId, InstructorRequestDTO dto) {
        Instructor instructor = repository.findById(instructorId)
                .orElseThrow(() -> new InstructorInvalidoException("No existe ningún instructor con ese id"));

        validarInstructor(dto);

        Empresa empresa = empresaRepository.findById(dto.getEmpresa_id())
                .orElseThrow(() -> new RuntimeException("No existe una empresa con ese ID"));

        instructor.setNombre(dto.getNombre());
        instructor.setTitulo(dto.getTitulo());
        instructor.setPresentacion(dto.getPresentacion());
        instructor.setEmpresa(empresa);

        return repository.save(instructor);
    }

    public List<Instructor> listarInstuctores() {
        return repository.findAll();
    }

    private void validarInstructor(InstructorRequestDTO dto) {

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

        if (dto.getPresentacion() == null || dto.getPresentacion().isBlank()) {
            throw new RuntimeException("La descripción del instructor es obligatoria");
        }

        if (dto.getPresentacion().length() > 255) {
            throw new RuntimeException("La descripción no puede superar los 255 caracteres");
        }

        if (dto.getEmpresa_id() == null) {
            throw new RuntimeException("La empresa es obligatoria");
        }
    }

}
