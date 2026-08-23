package com.example.Proyecto_Final_Progra3.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="rol")
public class RolEntity {
    @Id 
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="rol_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name="nombre_rol", nullable = false, unique = true, length = 50)
    private String nombre;
}
