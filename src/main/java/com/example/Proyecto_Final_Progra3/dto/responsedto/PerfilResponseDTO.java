package com.example.Proyecto_Final_Progra3.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerfilResponseDTO {
    private Integer perfil_id;
    private String usuario;
    private String email;
    private String rol;
}
