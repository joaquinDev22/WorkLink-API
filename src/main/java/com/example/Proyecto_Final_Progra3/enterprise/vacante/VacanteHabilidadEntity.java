package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import com.example.Proyecto_Final_Progra3.enterprise.talento.HabilidadEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "vacante_habilidad")
public class VacanteHabilidadEntity {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private VacanteHabilidadId vacanteHabilidadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("habilidadId")
    @JoinColumn(name = "habilidad_id")
    private HabilidadEntity habilidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vacanteId")
    @JoinColumn(name = "vacante_id")
    private VacanteEntity vacante;
}
