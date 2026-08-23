package com.example.Proyecto_Final_Progra3.model.bootcamps;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Modalidad;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import com.example.Proyecto_Final_Progra3.model.enterprise.EmpresaEntity;
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
@Table(name="bootcamp")
@EntityListeners(com.example.Proyecto_Final_Progra3.config.auditoria.AuditoriaListener.class)
public class BootcampEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bootcamp_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(name="fecha_inicio", nullable = false)
    private LocalDate inicio;

    @Column(name="fecha_fin", nullable = false)
    private LocalDate fin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modalidad modalidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    @Column(nullable = false)
    private int cupos;

    private String logo;
    private String precio;
    private Double rating;

    @ElementCollection
    @CollectionTable(name = "bootcamp_etiquetas", joinColumns = @JoinColumn(name = "bootcamp_id"))
    @Column(name = "etiqueta")
    private java.util.List<String> etiquetas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private InstructorEntity instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private EmpresaEntity empresa;
}
