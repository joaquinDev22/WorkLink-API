package com.example.Proyecto_Final_Progra3.auth.perfil;

import com.example.Proyecto_Final_Progra3.bootcamps.bootcamp.CalificacionBootcampEntity;
import com.example.Proyecto_Final_Progra3.freelance.proyecto.CalificacionProyectoEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="calificacion_usuario")
public class CalificacionUsuarioEntity {
    @Id 
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="calificacion_usuario_id")
    @EqualsAndHashCode.Include
    private Integer calificacionUsuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="calificacion_id")
    private CalificacionProyectoEntity calificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="calificacion_bootcamp_id")
    private CalificacionBootcampEntity calificacionBootcamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perfil_id", nullable = false)
    private PerfilEntity perfil;
}
