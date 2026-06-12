package com.example.Proyecto_Final_Progra3.model.bootcamps;

import com.example.Proyecto_Final_Progra3.model.Employee;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.EstadoInscripcion;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="inscripcion_bootcamp")
public class Inscripcion {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="inscripcion_id")
    private Integer id;

    @Column(name="fecha_inscripcion")
    private LocalDate inscripcion;

    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado;

    @ManyToOne
    @JoinColumn(name="bootcamp_id")
    private Bootcamp bootcamp;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;
}
