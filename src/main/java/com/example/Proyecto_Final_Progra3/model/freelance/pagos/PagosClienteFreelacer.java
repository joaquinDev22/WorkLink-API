package com.example.Proyecto_Final_Progra3.model.freelance.pagos;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pagos_cliente_freelancer")
public class PagosClienteFreelacer {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagos_cliente_freelancer_id")
    @EqualsAndHashCode.Include
    private Integer pagoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id")
    private PagosEntity pagos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodos_cobro_freelancer_id")
    private MetodosCobroFreelancer metodosCobroFreelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodos_pago_cliente_id")
    private MetodosPagoCliente metodosPagoCliente;
}

