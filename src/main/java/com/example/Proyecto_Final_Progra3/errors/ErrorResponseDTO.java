package com.example.Proyecto_Final_Progra3.errors;

import java.time.LocalDate;
import java.util.List;

public record ErrorResponseDTO(
        String message,
        List<String> details,
        LocalDate date
) {}
