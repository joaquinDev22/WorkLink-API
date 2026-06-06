package com.example.Proyecto_Final_Progra3.model.bootcamps.inscripciones;

import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEmpresa;
import com.example.Proyecto_Final_Progra3.model.enums.EstadoInscripcion;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "inscripcion_bootcamp_empresa")
public class InscripcionBootcampEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inscripcion_id")
    private Integer inscripcionId;

    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;

    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado;

    @ManyToOne
    @JoinColumn(name = "bootcamp_id", nullable = false)
    private BootcampEmpresa bootcampEmpresa;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
