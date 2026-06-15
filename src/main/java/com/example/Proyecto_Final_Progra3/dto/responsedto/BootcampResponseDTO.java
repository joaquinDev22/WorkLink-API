package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Modalidad;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BootcampResponseDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private LocalDate inicio;
    private LocalDate fin;
    private Modalidad modalidad;
    private Integer cupos;
    private Tipo tipo;
    private Integer instructor_id;
}
