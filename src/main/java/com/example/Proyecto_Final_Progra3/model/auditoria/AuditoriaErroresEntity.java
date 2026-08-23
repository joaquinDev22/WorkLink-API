package com.example.Proyecto_Final_Progra3.model.auditoria;

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
@Table(name="auditoria_errores")
public class AuditoriaErroresEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auditoria_error_id")
    @EqualsAndHashCode.Include
    private Integer auditoriaErrorId;

    @Column(nullable = false, length = 100)
    private String procedimiento;

    @Column(nullable = false, length = 100)
    private String operacion;

    @Column(name = "mensaje_error", nullable = false, length = 255)
    private String mensajeError;

    @Column(name = "fecha_error", insertable = false, updatable = false)
    private LocalDateTime fechaError;
}
