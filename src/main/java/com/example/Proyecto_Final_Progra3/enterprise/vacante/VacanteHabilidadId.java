package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class VacanteHabilidadId {
    private Integer vacanteId;
    private Integer habilidadId;
}
