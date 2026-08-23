package com.example.Proyecto_Final_Progra3.dto.requestdto;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Modalidad;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record BootcampRequestDTO(
        @NotBlank(message = "El título del bootcamp es obligatorio")
        @Size(max = 100, message = "El título no puede superar los 100 caracteres")
        String titulo,

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        @NotNull(message = "La fecha de inicio es obligatoria")
        @FutureOrPresent(message = "La fecha de inicio debe ser presente o futura")
        LocalDate inicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        @Future(message = "La fecha de fin debe ser una fecha futura")
        LocalDate fin,

        @NotNull(message = "La modalidad es obligatoria")
        Modalidad modalidad,

        @NotNull(message = "El tipo de bootcamp es obligatorio")
        Tipo tipo,

        @NotNull(message = "El número de cupos es obligatorio")
        @Min(value = 1, message = "El bootcamp debe tener al menos 1 cupo disponible")
        Integer cupos,

        @NotNull(message = "El id del instructor no puede ser nulo") Integer instructor_id,
        @NotNull(message = "El id de la empresa no puede ser nulo") Integer empresa_id,
        String logo,
        String precio,
        Double rating,
        List<String> etiquetas
) {}

