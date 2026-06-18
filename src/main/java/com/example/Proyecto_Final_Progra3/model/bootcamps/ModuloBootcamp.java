package com.example.Proyecto_Final_Progra3.model.bootcamps;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Modulo_Bootcamp")
public class ModuloBootcamp {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "modulo_id")
    private Integer moduloId;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "link_clase")
    private String link;

    @Column(name = "horario_clase", length = 100)
    private String horario;

    @ManyToOne
    @JoinColumn(name = "bootcamp_id")
    private Bootcamp bootcamp;
}
