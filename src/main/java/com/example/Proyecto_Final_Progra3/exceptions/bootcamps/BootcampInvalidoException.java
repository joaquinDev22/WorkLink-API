package com.example.Proyecto_Final_Progra3.exceptions.bootcamps;

import com.example.Proyecto_Final_Progra3.exceptions.domain.BadRequestException;

public class BootcampInvalidoException extends BadRequestException {
    public BootcampInvalidoException(String message) {
        super(message);
    }
}
