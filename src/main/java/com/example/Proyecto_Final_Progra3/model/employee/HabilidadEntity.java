package com.example.Proyecto_Final_Progra3.model.employee;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="habilidad")
public class HabilidadEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="habilidad_id")
    @EqualsAndHashCode.Include
    private Integer habilidadId;

    @Column(name="nombre_habilidad", nullable = false, unique = true, length = 100)
    private String nombreHabilidad;
}
