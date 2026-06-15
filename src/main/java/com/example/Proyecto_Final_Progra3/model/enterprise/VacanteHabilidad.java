package com.example.Proyecto_Final_Progra3.model.enterprise;

import com.example.Proyecto_Final_Progra3.model.employee.Habilidad;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vacante_habilidad")
public class VacanteHabilidad {
    @EmbeddedId
    private VacanteHabilidadId vacanteHabilidadId;

    @ManyToOne
    @MapsId("habilidadId")
    @JoinColumn(name = "habilidad_id")
    private Habilidad habilidad;

    @ManyToOne
    @MapsId("vacanteId")
    @JoinColumn(name = "vacante_id")
    private Vacante vacante;
}
