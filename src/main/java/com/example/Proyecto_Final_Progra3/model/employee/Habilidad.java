package com.example.Proyecto_Final_Progra3.model.employee;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="habilidad")
public class Habilidad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="habilidad_id")
    private Integer habilidadId;

    @Column(name="nombre_habilidad", nullable = false, unique = true, length = 100)
    private String nombreHabilidad;
}
