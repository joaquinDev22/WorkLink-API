package com.example.Proyecto_Final_Progra3.DTO.requestdto;

import com.example.Proyecto_Final_Progra3.model.enums.Rol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilRequestDTO {
    private String usuario;
    private String password;
    private Rol rol;
}
