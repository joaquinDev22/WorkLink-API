package com.example.Proyecto_Final_Progra3.model.enterprise;

import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="recruiter")
public class RecruiterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recruiter_id")
    @EqualsAndHashCode.Include
    private Integer recruiterId;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(nullable = false, unique = true, length = 50)
    private String email;
    
    @Column(name="fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(length = 50)
    private String area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="empresa_id")
    private EmpresaEntity empresa;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perfil_id", unique = true)
    private PerfilEntity perfil;

    /** Cuándo la empresa lo vinculó. */
    @Column(name="fecha_vinculacion")
    private LocalDateTime fechaVinculacion;

    /** false hasta que el recruiter ve el aviso de que fue contratado. */
    @Column(name="vinculacion_vista", nullable = false)
    @Builder.Default
    private boolean vinculacionVista = false;

    /** Cuándo pidió la baja; null si no la pidió. */
    @Column(name="baja_solicitada_en")
    private LocalDateTime bajaSolicitadaEn;

    /** Días que tiene la empresa para responder antes de que el recruiter pueda irse por su cuenta. */
    public static final int DIAS_RESPUESTA_BAJA = 15;

    public boolean tieneBajaSolicitada() {
        return bajaSolicitadaEn != null;
    }

    /** Desde cuándo puede desvincularse solo (si la empresa no respondió). */
    public LocalDateTime fechaBajaHabilitada() {
        return bajaSolicitadaEn == null ? null : bajaSolicitadaEn.plusDays(DIAS_RESPUESTA_BAJA);
    }

    public boolean puedeDarseDeBajaSolo(LocalDateTime ahora) {
        return bajaSolicitadaEn != null && !ahora.isBefore(fechaBajaHabilitada());
    }
}
