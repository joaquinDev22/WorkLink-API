package com.example.Proyecto_Final_Progra3.auditoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="auditoria_operaciones")
public class AuditoriaOperacionesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auditoria_id")
    @EqualsAndHashCode.Include
    private Integer auditoriaId;

    @Column(name = "tabla_afectada", nullable = false, length = 100)
    private String tablaAfectada;

    @Column(nullable = false, length = 50)
    private String operacion;

    @Column(name = "id_registro_afectado", nullable = false)
    private Integer idRegistroAfectado;

    @Column(nullable = false, length = 100)
    private String usuario;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(name = "fecha_operacion", insertable = false, updatable = false)
    private LocalDateTime fechaOperacion;
}
