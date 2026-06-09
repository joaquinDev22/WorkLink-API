package com.example.Proyecto_Final_Progra3.dto.requestdto;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Modalidad;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BootcampRequestDTO {
    private String titulo;
    private String descripcion;
    private LocalDate inicio;
    private LocalDate fin;
    private Modalidad modalidad;
    private Tipo tipo;
    private int cupos;
    private Integer instructor_id;
    private Integer empresa_id;

}
