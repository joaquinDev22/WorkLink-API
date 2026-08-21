package com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.InstructorRequestDTO;
import com.example.Proyecto_Final_Progra3.exceptions.bootcamps.InstructorInvalidoException;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampInterfaces.IInstructorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.Proyecto_Final_Progra3.dto.responsedto.InstructorResponseDTO;
import com.example.Proyecto_Final_Progra3.mappers.InstructorMapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class InstructorServiceImpl implements IInstructorService {
    private final InstructorRepository repository;
    private final EmpresaRepository empresaRepository;
    private final InstructorMapper mapper;

    @Transactional
    public InstructorResponseDTO crearInstructor(InstructorRequestDTO dto) {
        validarInstructor(dto);

        Instructor instructor = mapper.toEntity(dto);

        if (dto.tipo() != null && dto.tipo().equals(Tipo.EMPRESA)) {
            Empresa empresa = empresaRepository.findById(dto.empresa_id())
                    .orElseThrow(() -> new RuntimeException("No existe la empresa"));
            instructor.setEmpresa(empresa);
        }

        instructor = repository.save(instructor);
        return mapper.toDto(instructor);
    }

    @Transactional
    public void eliminarInstructor(Integer instructorId) {
        Instructor instructor = repository.findById(instructorId)
                .orElseThrow(() -> new InstructorInvalidoException("No existe un instructor con ese id"));
        repository.delete(instructor);
    }

    @Transactional
    public InstructorResponseDTO modificarInstructor(Integer instructorId, InstructorRequestDTO dto) {
        Instructor instructor = repository.findById(instructorId)
                .orElseThrow(() -> new InstructorInvalidoException("No existe ningún instructor con ese id"));

        validarInstructor(dto);

        mapper.updateEntityFromDto(dto, instructor);

        if (dto.tipo() != null && dto.tipo().equals(Tipo.EMPRESA)) {
            Empresa empresa = empresaRepository.findById(dto.empresa_id())
                    .orElseThrow(() -> new RuntimeException("No existe una empresa con ese ID"));
            instructor.setEmpresa(empresa);
        } else {
            instructor.setEmpresa(null);
        }

        instructor = repository.save(instructor);
        return mapper.toDto(instructor);
    }

    public List<InstructorResponseDTO> listarInstuctores() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void validarInstructor(InstructorRequestDTO dto) {

        if (dto.nombre() == null || dto.nombre().isBlank()) {
            throw new RuntimeException("El nombre del instructor es obligatorio");
        }

        if (dto.nombre().length() > 100) {
            throw new RuntimeException("El nombre no puede superar los 100 caracteres");
        }

        if (dto.titulo() == null || dto.titulo().isBlank()) {
            throw new RuntimeException("El título del instructor es obligatorio");
        }

        if (dto.titulo().length() > 100) {
            throw new RuntimeException("El título no puede superar los 100 caracteres");
        }

        if (dto.presentacion() == null || dto.presentacion().isBlank()) {
            throw new RuntimeException("La descripción del instructor es obligatoria");
        }

        if (dto.presentacion().length() > 255) {
            throw new RuntimeException("La descripción no puede superar los 255 caracteres");
        }

        if (dto.empresa_id() == null) {
            throw new RuntimeException("La empresa es obligatoria");
        }
    }

}
