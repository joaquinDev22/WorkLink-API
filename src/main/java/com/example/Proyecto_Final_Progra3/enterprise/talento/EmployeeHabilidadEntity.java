package com.example.Proyecto_Final_Progra3.enterprise.talento;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="employee_habilidad")
public class EmployeeHabilidadEntity {
    @EmbeddedId
    @EqualsAndHashCode.Include
    @Builder.Default
    private EmployeeHabilidadId id = new EmployeeHabilidadId();

    private Integer nivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("habilidadId")
    @JoinColumn(name = "habilidad_id")
    private HabilidadEntity habilidad;
}
