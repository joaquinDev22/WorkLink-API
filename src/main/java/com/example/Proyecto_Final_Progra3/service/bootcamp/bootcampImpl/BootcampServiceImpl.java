package com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.BootcampRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.BootcampResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.bootcamps.BootcampInvalidoException;
import com.example.Proyecto_Final_Progra3.mappers.BootcampMapper;
import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository;
import com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampInterfaces.IBootcampService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class BootcampServiceImpl implements IBootcampService {

    private final BootcampRepository repository;
    private final InstructorRepository instructorRepository;
    private final BootcampMapper mapper;
    private final PerfilRepository perfilRepository;

    @Override
    @Transactional
    public BootcampResponseDTO crearBootcamp(BootcampRequestDTO dto) {
        validarBootcamp(dto);

        Instructor instructor = instructorRepository.findById(dto.instructor_id())
                .orElseThrow(() -> new BootcampInvalidoException("No existe un instructor con el ID: " + dto.instructor_id()));

        Bootcamp bootcamp = mapper.toEntity(dto);
        bootcamp.setInstructor(instructor);

        bootcamp.setTipo(instructor.getTipo() == Tipo.PARTICULAR ? Tipo.PARTICULAR : Tipo.EMPRESA);

        bootcamp = repository.save(bootcamp);
        return mapper.toDto(bootcamp);
    }

    @Override
    @Transactional
    public BootcampResponseDTO crearMiBootcamp(String username, BootcampRequestDTO dto) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new BootcampInvalidoException("Usuario no encontrado: " + username));

        Instructor instructor = instructorRepository.findByPerfil(perfil)
                .orElseThrow(() -> new BootcampInvalidoException("Solo los instructores pueden crear bootcamps"));

        // Como los Records son inmutables, creamos una nueva instancia pasando el instructor_id e empresa_id asignados
        BootcampRequestDTO dtoConInstructor = new BootcampRequestDTO(
                dto.titulo(),
                dto.descripcion(),
                dto.inicio(),
                dto.fin(),
                dto.modalidad(),
                dto.tipo(),
                dto.cupos(),
                instructor.getId(), // Asignamos el ID del instructor
                1,                  // Asignamos empresa_id placeholder
                dto.logo(),
                dto.precio(),
                dto.rating(),
                dto.etiquetas()
        );

        return crearBootcamp(dtoConInstructor);
    }

    @Override
    @Transactional
    public void eliminarBootcamp(Integer id) {
        Bootcamp bootcamp = repository.findById(id)
                .orElseThrow(() -> new BootcampInvalidoException("No existe un bootcamp con el ID: " + id));

        repository.delete(bootcamp);
    }

    @Override
    @Transactional
    public BootcampResponseDTO modificarBootcamp(Integer id, BootcampRequestDTO dto) {
        Bootcamp bootcamp = repository.findById(id)
                .orElseThrow(() -> new BootcampInvalidoException("No existe un bootcamp con el ID: " + id));

        validarBootcamp(dto);

        Instructor instructor = instructorRepository.findById(dto.instructor_id())
                .orElseThrow(() -> new BootcampInvalidoException("No existe un instructor con el ID: " + dto.instructor_id()));

        mapper.updateEntityFromDto(dto, bootcamp);
        bootcamp.setInstructor(instructor);

        bootcamp = repository.save(bootcamp);
        return mapper.toDto(bootcamp);
    }

    @Override
    public List<BootcampResponseDTO> listarBootcamps() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BootcampResponseDTO> listarMisBootcamps(String username) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new BootcampInvalidoException("Usuario no encontrado: " + username));

        Instructor instructor = instructorRepository.findByPerfil(perfil)
                .orElseThrow(() -> new BootcampInvalidoException("Solo los instructores pueden listar sus bootcamps"));

        return repository.findByInstructor(instructor).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BootcampResponseDTO buscarPorId(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new BootcampInvalidoException("No existe un Bootcamp con el ID: " + id));
    }

    @Override
    public List<BootcampResponseDTO> listarDisponibles() {
        return repository.findByCuposGreaterThanAndInicioGreaterThanEqual(0, LocalDate.now()).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BootcampResponseDTO> listarPorModalidad(String modalidad) {
        if (modalidad == null || modalidad.isBlank()) {
            throw new BootcampInvalidoException("La modalidad es obligatoria");
        }
        return repository.findByModalidad(modalidad).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    private void validarBootcamp(BootcampRequestDTO dto) {
        if (dto.titulo() == null || dto.titulo().isBlank()) {
            throw new BootcampInvalidoException("El título es obligatorio");
        }
        if (dto.titulo().length() > 100) {
            throw new BootcampInvalidoException("El título no puede superar los 100 caracteres");
        }
        if (dto.descripcion() == null || dto.descripcion().isBlank()) {
            throw new BootcampInvalidoException("La descripción es obligatoria");
        }
        if (dto.descripcion().length() > 255) {
            throw new BootcampInvalidoException("La descripción no puede superar los 255 caracteres");
        }
        if (dto.inicio() == null) {
            throw new BootcampInvalidoException("La fecha de inicio es obligatoria");
        }
        if (dto.fin() == null) {
            throw new BootcampInvalidoException("La fecha de fin es obligatoria");
        }
        if (!dto.inicio().isAfter(LocalDate.now())) {
            throw new BootcampInvalidoException("La fecha de inicio debe ser posterior a la fecha actual");
        }
        if (!dto.fin().isAfter(dto.inicio())) {
            throw new BootcampInvalidoException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        if (dto.modalidad() == null) {
            throw new BootcampInvalidoException("La modalidad es obligatoria");
        }
        if (dto.cupos() <= 0) {
            throw new BootcampInvalidoException("Los cupos deben ser mayores a cero");
        }
        if (dto.instructor_id() == null) {
            throw new BootcampInvalidoException("El instructor es obligatorio");
        }
    }
}