package com.example.Proyecto_Final_Progra3.exceptions.domain;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
