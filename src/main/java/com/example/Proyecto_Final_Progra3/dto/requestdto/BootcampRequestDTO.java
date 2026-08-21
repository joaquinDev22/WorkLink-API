package com.example.Proyecto_Final_Progra3.dto.requestdto;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Modalidad;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record BootcampRequestDTO(
        String titulo,
        String descripcion,
        LocalDate inicio,
        LocalDate fin,
        Modalidad modalidad,
        Tipo tipo,
        Integer cupos,
        @NotNull(message = "El id del instructor no puede ser nulo") Integer instructor_id,
        @NotNull(message = "El id de la empresa no puede ser nulo") Integer empresa_id,
        String logo,
        String precio,
        Double rating,
        List<String> etiquetas
) {}
