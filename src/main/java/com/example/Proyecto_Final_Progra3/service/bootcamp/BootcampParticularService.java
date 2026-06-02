package com.example.Proyecto_Final_Progra3.service.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.requestdto.BootcampParticularRequestDTO;
import com.example.Proyecto_Final_Progra3.exceptions.BootcampInvalidoException;
import com.example.Proyecto_Final_Progra3.model.Instructor.InstructorParticular;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampParticular;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampParticularRepository;
import com.example.Proyecto_Final_Progra3.repository.instructor.InstructorParticularRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Service
public class BootcampParticularService {
    private final BootcampParticularRepository bootcampParticularRepository;
    private final InstructorParticularRepository instructorParticularRepository;

    public BootcampParticular crearBootcampParticular(BootcampParticularRequestDTO dto) {

        validarBootcampParticular(dto);

        InstructorParticular instructor = instructorParticularRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new RuntimeException("No existe un instructor particular con ese ID"));

        BootcampParticular bootcamp = new BootcampParticular();

        bootcamp.setTitulo(dto.getTitulo());
        bootcamp.setDescripcion(dto.getDescripcion());
        bootcamp.setFechaInicio(dto.getFechaInicio());
        bootcamp.setFechaFin(dto.getFechaFin());
        bootcamp.setModalidad(dto.getModalidad());
        bootcamp.setCupos(dto.getCupos());
        bootcamp.setInstructorParticular(instructor);

        return bootcampParticularRepository.save(bootcamp);
    }

    public BootcampParticular modificarBootcampParticular(Integer id, BootcampParticularRequestDTO dto) {

        BootcampParticular bootcamp = bootcampParticularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp particular con ese ID"));

        validarBootcampParticular(dto);

        InstructorParticular instructor = instructorParticularRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new RuntimeException("No existe un instructor particular con ese ID"));

        bootcamp.setTitulo(dto.getTitulo());
        bootcamp.setDescripcion(dto.getDescripcion());
        bootcamp.setFechaInicio(dto.getFechaInicio());
        bootcamp.setFechaFin(dto.getFechaFin());
        bootcamp.setModalidad(dto.getModalidad());
        bootcamp.setCupos(dto.getCupos());
        bootcamp.setInstructorParticular(instructor);

        return bootcampParticularRepository.save(bootcamp);
    }

    public List<BootcampParticular>listarBootcampsParticulares()
    {
        return bootcampParticularRepository.findAll();
    }

    //Metodo de validación del bootcampParticular
    private void validarBootcampParticular(BootcampParticularRequestDTO dto) {
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
        if (dto.getFechaInicio() == null) {
            throw new BootcampInvalidoException("La fecha de inicio es obligatoria");
        }
        if (dto.getFechaFin() == null) {
            throw new BootcampInvalidoException("La fecha de fin es obligatoria");
        }
        if (!dto.getFechaInicio().isAfter(LocalDate.now())) {
            throw new BootcampInvalidoException("La fecha de inicio debe ser posterior a la fecha actual");
        }
        if (!dto.getFechaFin().isAfter(dto.getFechaInicio())) {
            throw new BootcampInvalidoException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        if (dto.getModalidad() == null) {
            throw new BootcampInvalidoException("La modalidad es obligatoria");
        }
        if (dto.getCupos() == null || dto.getCupos() <= 0) {
            throw new BootcampInvalidoException("Los cupos deben ser mayores a cero");
        }
        if (dto.getInstructorId() == null) {
            throw new BootcampInvalidoException("El instructor particular es obligatorio");
        }
    }
}
