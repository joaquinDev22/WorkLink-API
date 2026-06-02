package com.example.Proyecto_Final_Progra3.model.freelance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="cliente_particular")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cliente_particular_id")
    private int cliente_id;

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
