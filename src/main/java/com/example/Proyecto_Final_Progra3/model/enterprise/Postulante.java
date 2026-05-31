package com.example.Proyecto_Final_Progra3.model.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Postulante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="employee_id")
    private int postulante_id;
}
