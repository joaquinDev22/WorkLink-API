package com.example.Proyecto_Final_Progra3.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private List<String> details;
    private LocalDate date;
}
