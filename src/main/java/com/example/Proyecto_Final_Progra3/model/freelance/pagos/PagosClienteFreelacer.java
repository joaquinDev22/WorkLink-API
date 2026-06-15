package com.example.Proyecto_Final_Progra3.model.freelance.pagos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pagos_cliente_freelancer")
public class PagosClienteFreelacer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagos_cliente_freelancer_id")
    private Integer pagoId;

    @ManyToOne
    @JoinColumn(name = "pago_id")
    private Pagos pagos;

    @ManyToOne
    @JoinColumn(name = "metodos_cobro_freelancer_id")
    private MetodosCobroFreelancer metodosCobroFreelancer;

    @ManyToOne
    @JoinColumn(name = "metodos_pago_cliente_id")
    private MetodosPagoCliente metodosPagoCliente;
}
