package com.example.Proyecto_Final_Progra3.model.enterprise;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "vacante")
public class Vacante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vacante_id")
    private Integer vacanteId;

    @Column(nullable = false, length = 50)
    private String puesto;

    @Column(nullable = false, length = 250)
    private String descripcion;

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDate publicacion;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate finalizacion;

    private String logo;
    private String ubicacion;
    
    @Column(name = "tipo_ubicacion")
    private String tipoUbicacion; // Remote, On-site, Hybrid
    
    @Column(name = "tipo_vacante")
    private String tipoVacante; // Full-time, Part-time, Contract, Freelance
    
    private String salario;

    @ElementCollection
    @CollectionTable(name = "vacante_etiquetas", joinColumns = @JoinColumn(name = "vacante_id"))
    @Column(name = "etiqueta")
    private java.util.List<String> etiquetas;

    @ManyToOne
    @JoinColumn(name = "empresa_id",  nullable = false)
    private Empresa empresa;
}
