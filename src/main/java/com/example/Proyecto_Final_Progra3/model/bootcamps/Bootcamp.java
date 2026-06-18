package com.example.Proyecto_Final_Progra3.model.bootcamps;

import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Modalidad;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bootcamp")
@EntityListeners(com.example.Proyecto_Final_Progra3.config.auditoria.AuditoriaListener.class)
public class Bootcamp {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bootcamp_id")
    private Integer id;

    private String titulo;
    private String descripcion;

    @Column(name="fecha_inicio")
    private LocalDate inicio;

    @Column(name="fecha_fin")
    private LocalDate fin;

    @Enumerated(EnumType.STRING)
    private Modalidad modalidad;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    private int cupos;

    private String logo;
    private String precio;
    private Double rating;

    @ElementCollection
    @CollectionTable(name = "bootcamp_etiquetas", joinColumns = @JoinColumn(name = "bootcamp_id"))
    @Column(name = "etiqueta")
    private java.util.List<String> etiquetas;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
