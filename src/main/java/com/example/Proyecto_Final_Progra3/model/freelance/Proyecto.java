package com.example.Proyecto_Final_Progra3.model.freelance;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name="proyecto")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proyecto_id")
    private Integer id;

    @Column(name = "area_proyecto")
    private String area;

    private String descripcion;

    @Column(name = "fecha_creacion")
    private LocalDate fechaPublicacion;

    @Column(name = "presupuesto_max")
    private Integer presupuestoMax;

    @Column(name = "presupuesto_min")
    private Integer presupuestoMin;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Client client;
}
