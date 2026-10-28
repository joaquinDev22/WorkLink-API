package com.example.Proyecto_Final_Progra3.model.freelance.proyecto;

import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoPostulacionProyecto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="postulacion_proyecto")
public class PostulacionProyectoEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_proy_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(length = 500)
    private String descripcion;

    @Column(name="valor_propuesto", nullable = false, precision = 10, scale = 2)
    private BigDecimal costo;

    @Column(name="fecha_postulacion", nullable = false, updatable = false)
    private LocalDate fechaPostulacion;

    @Enumerated(EnumType.STRING)
    @Column(name="estado_postulacion", nullable = false)
    @Builder.Default
    private EstadoPostulacionProyecto estado = EstadoPostulacionProyecto.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="proyecto_id", nullable = false)
    private ProyectoEntity proyecto;
}
