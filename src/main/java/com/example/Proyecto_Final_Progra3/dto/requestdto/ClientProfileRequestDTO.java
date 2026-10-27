package com.example.Proyecto_Final_Progra3.dto.requestdto;

import java.time.LocalDate;

public record ClientProfileRequestDTO(
        String firstName,
        String lastName,
        String phoneNumber,
        String country,
        LocalDate fechaNacimiento
) {}

