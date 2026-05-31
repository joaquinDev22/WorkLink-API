package com.example.Proyecto_Final_Progra3.model.freelance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Freelancer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="employee_id")
    private int freelancerId;

    @Column(name="nombre")
    private String firstName;

    @Column(name="apellido")
    private String lastName;

    private String email;

    @Column(name="fecha_nacimiento")
    private LocalDate dateOfBirth;

    private boolean freelance = true;
    private boolean enterprise = false;
}
