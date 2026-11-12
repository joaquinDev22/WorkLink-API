package com.example.Proyecto_Final_Progra3.model.freelance.pagos;

import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.HitoProyectoEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.MedioDePago;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.Moneda;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pagos")
public class PagosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pago_id")
    @EqualsAndHashCode.Include
    private Integer pagosId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Moneda moneda;

    @Column(nullable = false)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "medio_de_pago", nullable = false)
    private MedioDePago medioDePago;

    // ManyToOne: con pagos por hito, un proyecto tiene varios pagos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private ProyectoEntity proyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hito_id")
    private HitoProyectoEntity hito;

    @Column(name = "mp_payment_id", unique = true, length = 30)
    private String mpPaymentId;   // id del pago en Mercado Pago: evita registrar dos veces el mismo pago (RNF09)

    @Column(nullable = false, updatable = false)
    @Builder.Default   // WL-39: fecha de acreditación; los inserts por SQL usan el DEFAULT de la columna
    private LocalDateTime fecha = LocalDateTime.now();
}
