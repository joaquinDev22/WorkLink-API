package com.example.Proyecto_Final_Progra3.model.auditoria;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name="auditoria_operaciones")
public class AuditoriaOperaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auditoria_id")
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
