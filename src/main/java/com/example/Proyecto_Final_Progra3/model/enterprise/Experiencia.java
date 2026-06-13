package com.example.Proyecto_Final_Progra3.model.enterprise;

import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name="experiencia")
public class Experiencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experiencia_id")
    private Integer id;

    private String titulo;
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee empleado;
}
