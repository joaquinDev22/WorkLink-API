package com.example.Proyecto_Final_Progra3.model.freelance.proyecto;

import com.example.Proyecto_Final_Progra3.model.freelance.enums.EstadoHitoProyecto;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name="hito_proyecto")
public class HitoProyecto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hito_id")
    private Integer hitoId;

    @Column(nullable = false, length = 100)
    private String titulo;

    private String descripcion;
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    private EstadoHitoProyecto estado;

    @Column(name="fecha_limite")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name="proyecto_id")
    private Proyecto proyecto;
}
