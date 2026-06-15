package com.example.Proyecto_Final_Progra3.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogginRequestDTO {
    @NotBlank(message = "El usuario no puede estar vacio")
    private String usuario;
    @NotBlank(message = "La contrasena no puede estar vacia")
    private String password;
}
