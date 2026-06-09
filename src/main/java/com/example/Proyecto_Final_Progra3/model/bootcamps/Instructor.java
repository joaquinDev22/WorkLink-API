package com.example.Proyecto_Final_Progra3.model.bootcamps;

import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="instructor")
public class Instructor {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="instructor_id")
    private Integer id;
    private String nombre;
    private String apellido;
    private String titulo;
    private String presentacion;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @OneToOne
    @JoinColumn(name="perfil_id")
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name="empresa_id")
    private Empresa empresa;
}
