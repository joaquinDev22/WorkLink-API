package com.example.Proyecto_Final_Progra3.model.enterprise;

import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "portfolio")
public class Portfolio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private Integer portfolioId;

    @Column(name = "github", length = 50)
    private String githubLink;

    @Column(name = "linkedin", length = 50)
    private String linkedinLink;

    @Lob
    @Column(name = "cv", columnDefinition = "LONGBLOB")
    private byte[] cv;

    @Column(nullable = false, length = 250)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
