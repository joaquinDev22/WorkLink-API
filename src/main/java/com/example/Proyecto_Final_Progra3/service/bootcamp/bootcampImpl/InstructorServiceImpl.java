package com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.InstructorRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.InstructorResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.bootcamps.InstructorInvalidoException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.mappers.InstructorMapper;
import com.example.Proyecto_Final_Progra3.model.bootcamps.InstructorEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import com.example.Proyecto_Final_Progra3.model.enterprise.EmpresaEntity;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampInterfaces.IInstructorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        InstructorEntity instructor = mapper.toEntity(dto);

        if (dto.tipo() != null && dto.tipo().equals(Tipo.EMPRESA)) {
            EmpresaEntity empresa = empresaRepository.findById(dto.empresa_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + dto.empresa_id()));
            instructor.setEmpresa(empresa);
        }

        instructor = repository.save(instructor);
        return mapper.toDto(instructor);
    }

    @Transactional
    public void eliminarInstructor(Integer instructorId) {
        InstructorEntity instructor = repository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un instructor con el ID: " + instructorId));
        repository.delete(instructor);
    }

    @Transactional
    public InstructorResponseDTO modificarInstructor(Integer instructorId, InstructorRequestDTO dto) {
        InstructorEntity instructor = repository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un instructor con el ID: " + instructorId));

        validarInstructor(dto);

        mapper.updateEntityFromDto(dto, instructor);

        if (dto.tipo() != null && dto.tipo().equals(Tipo.EMPRESA)) {
            EmpresaEntity empresa = empresaRepository.findById(dto.empresa_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + dto.empresa_id()));
            instructor.setEmpresa(empresa);
        } else {
            instructor.setEmpresa(null);
        }

        instructor = repository.save(instructor);
        return mapper.toDto(instructor);
    }

    public List<InstructorResponseDTO> listarInstuctores() {
        return mapper.toDtoList(repository.findAll());
    }

    public void validarInstructor(InstructorRequestDTO dto) {

        if (dto.nombre() == null || dto.nombre().isBlank()) {
            throw new InstructorInvalidoException("El nombre del instructor es obligatorio");
        }

        if (dto.nombre().length() > 100) {
            throw new InstructorInvalidoException("El nombre no puede superar los 100 caracteres");
        }

        if (dto.titulo() == null || dto.titulo().isBlank()) {
            throw new InstructorInvalidoException("El título del instructor es obligatorio");
        }

        if (dto.titulo().length() > 100) {
            throw new InstructorInvalidoException("El título no puede superar los 100 caracteres");
        }

        if (dto.presentacion() == null || dto.presentacion().isBlank()) {
            throw new InstructorInvalidoException("La descripción del instructor es obligatoria");
        }

        if (dto.presentacion().length() > 255) {
            throw new InstructorInvalidoException("La descripción no puede superar los 255 caracteres");
        }

        if (dto.empresa_id() == null) {
            throw new InstructorInvalidoException("La empresa es obligatoria");
        }
    }
}
