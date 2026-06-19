package com.example.Proyecto_Final_Progra3.model.enterprise;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name="recruiter")
public class Recruiter{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recruiter_id")
    private Integer recruiterId;

    private String nombre;
    private String apellido;
    private String email;
    
    @Column(name="fecha_nacimiento")
    private java.time.LocalDate fechaNacimiento;
    
    private String area;

    @ManyToOne
    @JoinColumn(name="empresa_id")
    private Empresa empresa;

    @OneToOne
    @JoinColumn(name="perfil_id")
    private com.example.Proyecto_Final_Progra3.model.Perfil perfil;
}
