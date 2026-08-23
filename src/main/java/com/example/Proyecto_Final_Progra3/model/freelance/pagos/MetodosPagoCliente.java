package com.example.Proyecto_Final_Progra3.model.freelance.pagos;

import com.example.Proyecto_Final_Progra3.model.freelance.ClientEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EmpresaTarjeta;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EntidadBancaria;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.TipoDeTajeta;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "Metodos_Pago_Cliente")
public class MetodosPagoCliente {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodos_pago_cliente_id")
    @EqualsAndHashCode.Include
    private Integer metodoId;

    @Column(nullable = false, length = 50)
    private String titular;

    @Column(nullable = false, unique = true, length = 50)
    private String dni;

    @Enumerated(EnumType.STRING)
    @Column(name = "entidad_bancaria")
    private EntidadBancaria entidadBancaria;

    @Column(nullable = false, unique = true, length = 50)
    private String cvu;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tarjeta")
    private TipoDeTajeta tipoDeTajeta;

    @Enumerated(EnumType.STRING)
    @Column(name = "empresa_tarjeta")
    private EmpresaTarjeta  empresaTarjeta;

    @Column(name = "numero_tarjeta", nullable = false, unique = true, length = 50)
    private String numeroTarjeta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private ClientEntity client;
}

