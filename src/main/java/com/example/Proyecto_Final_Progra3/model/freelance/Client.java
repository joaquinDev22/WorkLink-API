package com.example.Proyecto_Final_Progra3.model.freelance;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name="cliente_particular")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cliente_particular_id")
    private Integer clienteId;

    @Column(name="nombre")
    private String firstName;

    @Column(name="apellido")
    private String lastName;
    private String email;

    @Column(name="telefono")
    private String phoneNumber;

    @Column(name="pais")
    private String country;
}
