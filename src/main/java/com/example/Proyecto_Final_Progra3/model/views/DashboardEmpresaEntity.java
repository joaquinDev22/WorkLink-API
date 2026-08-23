package com.example.Proyecto_Final_Progra3.model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "vw_dashboard_empresa")
@Getter
@NoArgsConstructor
public class DashboardEmpresaEntity {
    @Id
    private Integer empresaId;
    private String empresaNombre;
    private Long totalVacantesPublicadas;
    private Long totalPostulacionesRecibidas;
}
