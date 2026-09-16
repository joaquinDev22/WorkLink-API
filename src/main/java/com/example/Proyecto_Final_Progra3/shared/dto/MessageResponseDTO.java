package com.example.Proyecto_Final_Progra3.shared.dto;

import java.time.LocalDateTime;

public record MessageResponseDTO(
        String message,
        LocalDateTime timeStamp
) {
    public MessageResponseDTO(String message) {
        this(message, LocalDateTime.now());
    }
}
