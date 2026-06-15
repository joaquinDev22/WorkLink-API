package com.example.Proyecto_Final_Progra3.model.freelance.pagos;

import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.freelance.enums.EntidadBancaria;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "metodos_cobro_freelancer")
public class MetodosCobroFreelancer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodos_cobro_freelancer_id")
    private Integer cobroId;

    @Column(nullable = false, length = 100)
    private String titular;

    @Column(nullable = false, unique = true, length = 50)
    private String dni;

    @Enumerated(EnumType.STRING)
    @Column(name = "entidad_bancaria")
    private EntidadBancaria entidadBancaria;

    @Column(name = "numero_cuenta",nullable = false, length = 100)
    private String numeroDeCuenta;

    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
