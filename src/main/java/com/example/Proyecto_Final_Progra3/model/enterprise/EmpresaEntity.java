package com.example.Proyecto_Final_Progra3.model.enterprise;

import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.util.UuidV7Generator;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="Empresa")
public class EmpresaEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="empresa_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Builder.Default
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid = UuidV7Generator.generate();

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(name = "sitio_web", length = 150)
    private String sitio_web;

    @Column(length = 150)
    private String ubicacion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perfil_id", unique = true)
    private PerfilEntity perfil;
}
