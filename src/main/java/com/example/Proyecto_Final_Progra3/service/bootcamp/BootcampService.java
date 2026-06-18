package com.example.Proyecto_Final_Progra3.service.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.requestdto.BootcampRequestDTO;
import com.example.Proyecto_Final_Progra3.exceptions.bootcamps.BootcampInvalidoException;
import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

import com.example.Proyecto_Final_Progra3.dto.responsedto.BootcampResponseDTO;
import com.example.Proyecto_Final_Progra3.mappers.BootcampMapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class BootcampService {
    private final BootcampRepository repository;
    private final InstructorRepository instructorRepository;
    private final BootcampMapper mapper;
    private final PerfilRepository perfilRepository;



    @Transactional
    public BootcampResponseDTO crearBootcamp(BootcampRequestDTO dto) {

        validarBootcamp(dto);

        Instructor instructor = instructorRepository.findById(dto.getInstructor_id())
                .orElseThrow(() -> new RuntimeException("No existe un instructor particular con ese ID"));

        Bootcamp bootcamp = mapper.toEntity(dto);
        bootcamp.setInstructor(instructor);
        if (instructor.getTipo() == Tipo.PARTICULAR) {
            bootcamp.setTipo(Tipo.PARTICULAR);
        } else {
            bootcamp.setTipo(Tipo.EMPRESA);
        }

        bootcamp = repository.save(bootcamp);
        return mapper.toDto(bootcamp);
    }

    @Transactional
    public BootcampResponseDTO crearMiBootcamp(String username, BootcampRequestDTO dto) {
        Perfil perfil = perfilRepository
                .findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Instructor instructor = instructorRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo los instructores pueden crear bootcamps"));

        dto.setInstructor_id(instructor.getId());
        dto.setEmpresa_id(1); // placeholder para evitar ConstraintViolation

        return crearBootcamp(dto);
    }

    @Transactional
    public void eliminarBootcamp(Integer id) {

        Bootcamp bootcamp = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp empresarial con ese ID"));

        repository.delete(bootcamp);
    }

    @Transactional
    public BootcampResponseDTO modificarBootcamp(Integer id, BootcampRequestDTO dto) {

        Bootcamp bootcamp = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp particular con ese ID"));

        validarBootcamp(dto);

        Instructor instructor = instructorRepository.findById(dto.getInstructor_id())
                .orElseThrow(() -> new RuntimeException("No existe un instructor particular con ese ID"));

        mapper.updateEntityFromDto(dto, bootcamp);
        bootcamp.setInstructor(instructor);

        bootcamp = repository.save(bootcamp);
        return mapper.toDto(bootcamp);
    }

    public List<BootcampResponseDTO> listarBootcamps() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BootcampResponseDTO> listarMisBootcamps(String username) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Instructor instructor = instructorRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo los instructores pueden listar sus bootcamps"));
        return repository.findByInstructor(instructor).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    // Metodo de validación del bootcampParticular
    private void validarBootcamp(BootcampRequestDTO dto) {
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new BootcampInvalidoException("El título es obligatorio");
        }
        if (dto.getTitulo().length() > 100) {
            throw new BootcampInvalidoException("El título no puede superar los 100 caracteres");
        }
        if (dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
            throw new BootcampInvalidoException("La descripción es obligatoria");
        }
        if (dto.getDescripcion().length() > 255) {
            throw new BootcampInvalidoException("La descripción no puede superar los 255 caracteres");
        }
        if (dto.getInicio() == null) {
            throw new BootcampInvalidoException("La fecha de inicio es obligatoria");
        }
        if (dto.getFin() == null) {
            throw new BootcampInvalidoException("La fecha de fin es obligatoria");
        }
        if (!dto.getInicio().isAfter(LocalDate.now())) {
            throw new BootcampInvalidoException("La fecha de inicio debe ser posterior a la fecha actual");
        }
        if (!dto.getFin().isAfter(dto.getInicio())) {
            throw new BootcampInvalidoException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        if (dto.getModalidad() == null) {
            throw new BootcampInvalidoException("La modalidad es obligatoria");
        }
        if (dto.getCupos() <= 0) {
            throw new BootcampInvalidoException("Los cupos deben ser mayores a cero");
        }
        if (dto.getInstructor_id() == null) {
            throw new BootcampInvalidoException("El instructor particular es obligatorio");
        }
    }

    public BootcampResponseDTO buscarPorId(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new BootcampInvalidoException("No existe un Bootcamp con ese ID"));
    }

    public List<BootcampResponseDTO> listarDisponibles() {
        return repository.findByCuposGreaterThanAndInicioGreaterThanEqual(0, LocalDate.now()).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BootcampResponseDTO> listarPorModalidad(String modalidad) {
        if (modalidad == null) {
            throw new BootcampInvalidoException("La modalidad es obligatoria!");
        }
        return repository.findByModalidad(modalidad).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
