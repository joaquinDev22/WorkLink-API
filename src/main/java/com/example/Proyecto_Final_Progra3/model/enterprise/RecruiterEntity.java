package com.example.Proyecto_Final_Progra3.model.enterprise;

import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
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
@Table(name="recruiter")
public class RecruiterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recruiter_id")
    @EqualsAndHashCode.Include
    private Integer recruiterId;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(nullable = false, unique = true, length = 50)
    private String email;
    
    @Column(name="fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(length = 50)
    private String area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="empresa_id")
    private EmpresaEntity empresa;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perfil_id", unique = true)
    private PerfilEntity perfil;
}
