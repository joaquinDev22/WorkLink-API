package com.example.Proyecto_Final_Progra3.model.enterprise;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="entrevista")
public class EntrevistaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entrevista_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postulacion_id", nullable = false)
    private PostulacionVacanteEntity postulacion;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(length = 255)
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    @Builder.Default
    private EstadoEntrevista status = EstadoEntrevista.PROGRAMADA;

    /** Programada = no cancelada y todavía no ocurrió (las canceladas o pasadas no bloquean nada). */
    // Horario de oficina: de lunes a viernes, de 9 a 18 hs (la última entrevista empieza antes de las 18)
    public static final LocalTime INICIO_OFICINA = LocalTime.of(9, 0);
    public static final LocalTime FIN_OFICINA = LocalTime.of(18, 0);

    public static boolean esHorarioDeOficina(LocalDate fecha, LocalTime hora) {
        java.time.DayOfWeek dia = fecha.getDayOfWeek();
        return dia != java.time.DayOfWeek.SATURDAY && dia != java.time.DayOfWeek.SUNDAY
                && !hora.isBefore(INICIO_OFICINA) && hora.isBefore(FIN_OFICINA);
    }

    public boolean estaProgramada() {
        return status != EstadoEntrevista.CANCELADA && fecha != null && hora != null
                && !LocalDateTime.of(fecha, hora).isBefore(LocalDateTime.now());
    }
}
