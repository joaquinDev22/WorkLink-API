package com.example.Proyecto_Final_Progra3.enterprise.talento;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class EmployeeHabilidadId implements Serializable {
    private Integer employeeId;
    private Integer habilidadId;
}
