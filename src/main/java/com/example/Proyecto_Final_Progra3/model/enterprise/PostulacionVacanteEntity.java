package com.example.Proyecto_Final_Progra3.model.enterprise;

import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "postulacion_vacante")
public class PostulacionVacanteEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postulacion_id")
    @EqualsAndHashCode.Include
    private Integer postulacionVacanteId;

    @Column(name = "fecha_postulacion", nullable = false, updatable = false)
    private LocalDate fechaPostulacion;

    @Column(name = "estado_postulacion", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPostulacion estado;

    @Lob
    @Column(name = "cv", columnDefinition = "LONGBLOB")
    private byte[] cv;

    @Column(length = 250)
    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacante_id", nullable = false)
    private VacanteEntity vacante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;
}
