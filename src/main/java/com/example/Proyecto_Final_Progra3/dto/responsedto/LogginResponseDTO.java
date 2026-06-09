package com.example.Proyecto_Final_Progra3.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogginResponseDTO {

    private String mensaje;

    private String nombreUsuario;

    private String rol;

    private String token;
}
