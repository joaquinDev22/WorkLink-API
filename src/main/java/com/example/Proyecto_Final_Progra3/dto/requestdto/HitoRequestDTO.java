package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

// Sin monto: el monto acordado se reparte en partes iguales entre los hitos (D3)
public record HitoRequestDTO(
        @NotBlank(message = "El título del hito es obligatorio")
        @Size(max = 100, message = "El título no puede superar los 100 caracteres")
        String titulo,

        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        String descripcion,

        LocalDate fechaLimite
) {}
