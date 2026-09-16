package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "Calificacion_Bootcamp")
public class CalificacionBootcampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calificacion_id")
    @EqualsAndHashCode.Include
    private Integer calificacionId;

    @Column(name = "puntaje", nullable = false)
    private Integer puntaje;

    @Column(name = "comentario", nullable = false, length = 255)
    private String comentario;

    @Column(name = "fecha_comentario", nullable = false, updatable = false)
    private LocalDate fechaComentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bootcamp_id", nullable = false)
    private BootcampEntity bootcamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;
}
