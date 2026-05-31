package com.example.Proyecto_Final_Progra3.model.bootcamps;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;

import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
public abstract class Bootcamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bootcamp_id")
    private Integer bootcampId;
    private String titulo;
    private String descripcion;
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    private String modalidad;
    private Integer cupos;
    @Column(name = "instructor_id")
    private Integer instructorId;

}
