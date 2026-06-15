package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import lombok.Data;

@Data
public class InstructorResponseDTO {
    private Integer id;
    private String nombre;
    private String titulo;
    private String presentacion;
    private Tipo tipo;
    private Integer empresa_id;
}
