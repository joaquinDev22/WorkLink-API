package com.example.Proyecto_Final_Progra3.freelance.pagos;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "metodos_cobro_freelancer")
public class MetodosCobroFreelancer {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodos_cobro_freelancer_id")
    @EqualsAndHashCode.Include
    private Integer cobroId;

    @Column(nullable = false, length = 100)
    private String titular;

    @Column(nullable = false, unique = true, length = 50)
    private String dni;

    @Enumerated(EnumType.STRING)
    @Column(name = "entidad_bancaria")
    private EntidadBancaria entidadBancaria;

    @Column(name = "numero_cuenta", nullable = false, length = 100)
    private String numeroDeCuenta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;
}

