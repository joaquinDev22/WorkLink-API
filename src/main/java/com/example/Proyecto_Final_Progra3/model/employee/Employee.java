package com.example.Proyecto_Final_Progra3.model.employee;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name="employee")
public class Employee {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer employeeId;

    private String nombre;
    private String apellido;
    private String email;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
}
