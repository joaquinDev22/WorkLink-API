package com.example.Proyecto_Final_Progra3.model.bootcamps;

import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.EstadoInscripcion;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="inscripcion_bootcamp")
public class InscripcionEntity {

    @Id 
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="inscripcion_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name="fecha_inscripcion", nullable = false, updatable = false)
    private LocalDate inscripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInscripcion estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bootcamp_id", nullable = false)
    private BootcampEntity bootcamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", nullable = false)
    private EmployeeEntity employee;
}
