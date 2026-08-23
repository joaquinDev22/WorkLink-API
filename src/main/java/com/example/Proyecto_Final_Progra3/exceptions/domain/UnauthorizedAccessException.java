package com.example.Proyecto_Final_Progra3.exceptions.domain;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}