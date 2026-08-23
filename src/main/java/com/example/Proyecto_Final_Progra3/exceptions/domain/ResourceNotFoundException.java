package com.example.Proyecto_Final_Progra3.exceptions.domain;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
