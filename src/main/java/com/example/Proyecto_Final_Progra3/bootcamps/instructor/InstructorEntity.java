package com.example.Proyecto_Final_Progra3.bootcamps.instructor;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;
import com.example.Proyecto_Final_Progra3.bootcamps.bootcamp.Tipo;
import com.example.Proyecto_Final_Progra3.enterprise.empresa.EmpresaEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="instructor")
public class InstructorEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="instructor_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(length = 100)
    private String titulo;

    @Column(length = 500)
    private String presentacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perfil_id", unique = true)
    private PerfilEntity perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="empresa_id")
    private EmpresaEntity empresa;
}
