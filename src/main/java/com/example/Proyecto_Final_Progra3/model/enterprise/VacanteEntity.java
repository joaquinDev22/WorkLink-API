package com.example.Proyecto_Final_Progra3.model.enterprise;

import com.example.Proyecto_Final_Progra3.util.UuidV7Generator;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "vacante")
@EntityListeners(com.example.Proyecto_Final_Progra3.config.auditoria.AuditoriaListener.class)
public class VacanteEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vacante_id")
    @EqualsAndHashCode.Include
    private Integer vacanteId;

    @Builder.Default
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid = UuidV7Generator.generate();

    @Column(nullable = false, length = 50)
    private String puesto;

    @Column(nullable = false, length = 250)
    private String descripcion;

    @Column(name = "fecha_publicacion", nullable = false, updatable = false)
    private LocalDate publicacion;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate finalizacion;

    private String logo;
    private String ubicacion;
    
    @Column(name = "tipo_ubicacion")
    private String tipoUbicacion;

    @Column(name = "tipo_vacante")
    private String tipoVacante;
    
    private String salario;

    @Builder.Default
    @Column(name = "activa", nullable = false)
    private boolean activa = true;

    @ElementCollection
    @CollectionTable(name = "vacante_etiquetas", joinColumns = @JoinColumn(name = "vacante_id"))
    @Column(name = "etiqueta")
    private java.util.List<String> etiquetas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaEntity empresa;
}
