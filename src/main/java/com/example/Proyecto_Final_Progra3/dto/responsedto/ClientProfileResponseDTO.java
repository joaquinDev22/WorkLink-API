package com.example.Proyecto_Final_Progra3.dto.responsedto;

import java.time.LocalDate;

public record ClientProfileResponseDTO(
        Integer clienteId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String country,
        LocalDate fechaNacimiento,
        Integer perfilId,
        String username
) {}

