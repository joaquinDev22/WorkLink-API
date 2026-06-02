package com.example.Proyecto_Final_Progra3.service.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.requestdto.BootcampEmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.exceptions.BootcampInvalidoException;
import com.example.Proyecto_Final_Progra3.model.Instructor.InstructorEmpresa;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEmpresa;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.BootcampEmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.instructor.InstructorEmpresaRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BootcampEmpresaService {
    private final BootcampEmpresaRepository bootcampEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final InstructorEmpresaRepository instructorEmpresaRepository;

    public BootcampEmpresa guardar(BootcampEmpresaRequestDTO dto) {
        validarBootcampEmpresa(dto);

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("No existe la empresa"));

        InstructorEmpresa instructor = instructorEmpresaRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new RuntimeException("No existe el instructor"));

        BootcampEmpresa bootcamp = new BootcampEmpresa();
        bootcamp.setTitulo(dto.getTitulo());
        bootcamp.setDescripcion(dto.getDescripcion());
        bootcamp.setFechaInicio(dto.getFechaInicio());
        bootcamp.setFechaFin(dto.getFechaFin());
        bootcamp.setModalidad(dto.getModalidad());
        bootcamp.setCupos(dto.getCupos());
        bootcamp.setEmpresa(empresa);
        bootcamp.setInstructorEmpresa(instructor);

        return bootcampEmpresaRepository.save(bootcamp);
    }

    public void eliminarBootcampEmpresa(Integer id) {

        BootcampEmpresa bootcamp = bootcampEmpresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un bootcamp empresarial con ese ID"));

        bootcampEmpresaRepository.delete(bootcamp);
    }


    //Metodo de validación de creación de bootcampEmpresa
    private void validarBootcampEmpresa(BootcampEmpresaRequestDTO dto) {
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new BootcampInvalidoException("El título es obligatorio");
        }

        if (dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
            throw new BootcampInvalidoException("La descripción es obligatoria");
        }

        if (dto.getFechaInicio() == null) {
            throw new BootcampInvalidoException("La fecha de inicio es obligatoria");
        }

        if (dto.getFechaFin() == null) {
            throw new BootcampInvalidoException("La fecha de fin es obligatoria");
        }

        if (!dto.getFechaFin().isAfter(dto.getFechaInicio())) {
            throw new BootcampInvalidoException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        if (dto.getCupos() == null || dto.getCupos() <= 0) {
            throw new BootcampInvalidoException("Los cupos deben ser mayores a cero");
        }

        if (dto.getModalidad() == null) {
            throw new BootcampInvalidoException("La modalidad es obligatoria");
        }

        if (dto.getEmpresaId() == null) {
            throw new BootcampInvalidoException("La empresa es obligatoria");
        }

        if (dto.getInstructorId() == null) {
            throw new BootcampInvalidoException("El instructor es obligatorio");
        }
    }
}
