package com.example.Proyecto_Final_Progra3.model.employee;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="employee_habilidad")
public class EmployeeHabilidad {
    @EmbeddedId
    private EmployeeHabilidadId employeeHabilidadId = new EmployeeHabilidadId();

    private Integer nivel;
    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @MapsId("habilidadId")
    @JoinColumn(name = "habilidad_id")
    private Habilidad habilidad;

}
