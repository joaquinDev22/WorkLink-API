package com.example.Proyecto_Final_Progra3.service.bootcamp;


import com.example.Proyecto_Final_Progra3.dto.requestdto.BootcampRequestDTO;
import com.example.Proyecto_Final_Progra3.exceptions.bootcamps.BootcampInvalidoException;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BootcampService {
    private final BootcampRepository repository;
    private final InstructorRepository instructorRepository;

    public BootcampService(BootcampRepository repository,  InstructorRepository instructorRepository){
        this.repository = repository;
        this.instructorRepository = instructorRepository;
    }

    public Bootcamp crearBootcamp(BootcampRequestDTO dto) {

        validarBootcamp(dto);

        Instructor instructor = instructorRepository.findById(dto.getInstructor_id())
                .orElseThrow(() -> new RuntimeException("No existe un instructor particular con ese ID"));

        Bootcamp bootcamp = new Bootcamp();

        bootcamp.setTitulo(dto.getTitulo());
        bootcamp.setDescripcion(dto.getDescripcion());
        bootcamp.setInicio(dto.getInicio());
        bootcamp.setFin(dto.getFin());
        bootcamp.setModalidad(dto.getModalidad());
        bootcamp.setCupos(dto.getCupos());
        bootcamp.setInstructor(instructor);
        if(instructor.getTipo() == Tipo.PARTICULAR){
            bootcamp.setTipo(Tipo.PARTICULAR);
        }else{
            bootcamp.setTipo(Tipo.EMPRESA);
        }

        return repository.save(bootcamp);
    }

    public void eliminarBootcamp(Integer id) {

        Bootcamp bootcamp = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp empresarial con ese ID"));

        repository.delete(bootcamp);
    }

    public Bootcamp modificarBootcamp(Integer id, BootcampRequestDTO dto) {

        Bootcamp bootcamp = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp particular con ese ID"));

        validarBootcamp(dto);

        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un instructor particular con ese ID"));

        bootcamp.setTitulo(dto.getTitulo());
        bootcamp.setDescripcion(dto.getDescripcion());
        bootcamp.setInicio(dto.getInicio());
        bootcamp.setFin(dto.getFin());
        bootcamp.setModalidad(dto.getModalidad());
        bootcamp.setCupos(dto.getCupos());
        bootcamp.setInstructor(instructor);

        return repository.save(bootcamp);
    }

    public List<Bootcamp> listarBootcamps() {
        return repository.findAll();
    }

    //Metodo de validación del bootcampParticular
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

    public Bootcamp buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() ->
                new BootcampInvalidoException("No existe un Bootcamp con ese ID"));
    }

    public List<Bootcamp> listarDisponibles() {
        return repository.findByCuposGreaterThanAndInicioGreaterThanEqual(0, LocalDate.now());
    }

    public List<Bootcamp> listarPorModalidad(String modalidad) {
        if (modalidad == null) {
            throw new BootcampInvalidoException("La modalidad es obligatoria!");
        }
        return repository.findByModalidad(modalidad);
    }
}
