package com.example.Proyecto_Final_Progra3.model.enterprise;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@Entity
@Table(name="entrevista")
public class Entrevista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entrevista_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "postulacion_id", nullable = false)
    private PostulacionVacante postulacion;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(length = 255)
    private String link;
}
