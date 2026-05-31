package com.example.Proyecto_Final_Progra3.model.Instructor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.*;

@Getter
@Setter
@MappedSuperclass
public abstract class Instructor {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "instructor_id")
    private Integer instructorId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;
}
