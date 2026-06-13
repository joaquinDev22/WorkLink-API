package com.example.Proyecto_Final_Progra3.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="rol")
public class Rol {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="rol_id")
    private Integer id;

    @Column(name="nombre_rol")
    private String nombre;
}
