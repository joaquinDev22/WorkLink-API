package com.example.Proyecto_Final_Progra3.model.enterprise;

import com.example.Proyecto_Final_Progra3.model.Perfil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Empresa")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    private String nombre;
    private String descripcion;
    private String sitio_web;
    private String ubicacion;

    @OneToOne
    @JoinColumn(name="perfil_id",unique = true)
    private Perfil perfil;
}
