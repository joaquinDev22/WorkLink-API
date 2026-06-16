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

    private String duracion;
    
    @Column(name = "tipo_pago")
    private String tipoPago; // Fixed or Hourly

    @ElementCollection
    @CollectionTable(name = "proyecto_habilidades", joinColumns = @JoinColumn(name = "proyecto_id"))
    @Column(name = "habilidad")
    private java.util.List<String> habilidades;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Client client;
}
