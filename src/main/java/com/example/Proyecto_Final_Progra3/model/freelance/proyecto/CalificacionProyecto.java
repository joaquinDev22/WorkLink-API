package com.example.Proyecto_Final_Progra3.model.freelance.proyecto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name="calificacion_proyecto")
public class CalificacionProyecto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="calificacion_id")
    private Integer calificacionId;

    private Integer puntaje;
    private String comentario;

    @Column(name="fecha_comentario")
    private LocalDate fechaComentario;

    @ManyToOne
    @JoinColumn(name="proyecto_id")
    private Proyecto proyecto;
}
