package com.example.Proyecto_Final_Progra3.model.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.enums.TipoMedioCobro;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "freelancer_medios_cobro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FreelancerMedioCobroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "freelancer_id", nullable = false, unique = true)
    private Long freelancerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMedioCobro tipo;

    @Column(nullable = false, length = 120)
    private String titular;

    @Column(name = "documento_titular", length = 20)
    private String documentoTitular;

    @Column(name = "cbu_cvu", length = 22)
    private String cbuCvu;

    @Column(length = 40)
    private String alias;

    @Column(name = "mercado_pago_email", length = 150)
    private String mercadoPagoEmail;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
