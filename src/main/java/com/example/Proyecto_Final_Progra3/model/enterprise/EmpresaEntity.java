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
@Table(name="empresa")
public class EmpresaEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="empresa_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Builder.Default
    @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(name = "uuid", nullable = false, unique = true, updatable = false, length = 36)
    private UUID uuid = UuidV7Generator.generate();

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(name = "sitio_web", length = 150)
    private String sitio_web;

    @Column(length = 150)
    private String ubicacion;

    // Imágenes como data URL ("data:image/png;base64,..."): se muestran directo en <img src>
    @Column(columnDefinition = "MEDIUMTEXT")
    private String logo;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String banner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perfil_id", unique = true)
    private PerfilEntity perfil;
}
