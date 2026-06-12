package com.example.Proyecto_Final_Progra3.model.bootcamps;

import com.example.Proyecto_Final_Progra3.model.Employee;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Calificacion_Bootcamp")
public class CalificacionBootcamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calificacion_id")
    private Integer calificacionId;

    @Column(name = "puntaje", nullable = false)
    private Integer puntaje;

    @Column(name = "comentario", nullable = false, length = 255)
    private String comentario;

    @Column(name = "fecha_comentario", nullable = false)
    private LocalDate fechaComentario;

    @ManyToOne
    @JoinColumn(name = "bootcamp_id", nullable = false)
    private Bootcamp bootcamp;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
