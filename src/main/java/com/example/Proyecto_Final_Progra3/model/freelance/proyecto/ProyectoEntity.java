package com.example.Proyecto_Final_Progra3.model.freelance.proyecto;

import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.ClientEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoProyecto;
import com.example.Proyecto_Final_Progra3.util.UuidV7Generator;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="proyecto")
@EntityListeners(com.example.Proyecto_Final_Progra3.config.auditoria.AuditoriaListener.class)
public class ProyectoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proyecto_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Builder.Default
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid = UuidV7Generator.generate();

    @Column(name = "nombre_proyecto", nullable = false, length = 30)
    private String nombreProyecto;

    @Column(name = "area_proyecto", nullable = false, length = 10)
    private String area;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDate fechaPublicacion;

    @Column(name = "presupuesto_max", nullable = false)
    private BigDecimal presupuestoMax;

    @Column(name = "presupuesto_min", nullable = false)
    private BigDecimal presupuestoMin;

    @Column(length = 50)
    private String duracion;
    
    @Column(name = "tipo_pago", length = 50)
    private String tipoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProyecto estado;

    @ElementCollection
    @CollectionTable(name = "proyecto_habilidades", joinColumns = @JoinColumn(name = "proyecto_id"))
    @Column(name = "habilidad")
    private java.util.List<String> habilidades;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="freelancer_asignado_id")
    private EmployeeEntity freelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private ClientEntity client;
}
