package com.example.Proyecto_Final_Progra3.model;

import com.example.Proyecto_Final_Progra3.model.bootcamps.CalificacionBootcamp;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.CalificacionProyecto;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="calificacion_usuario")
public class CalificacionUsuario {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="calificacion_usuario_id")
    private Integer calficacionUsuaroId;

    @ManyToOne
    @JoinColumn(name="calificacion_id")
    private CalificacionProyecto calificacion;

    @ManyToOne
    @JoinColumn(name="calificacion_bootcamp_id")
    private CalificacionBootcamp calificacionBootcamp;

    @ManyToOne
    @JoinColumn(name="perfil_id")
    private Perfil perfil;
}
