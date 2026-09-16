package com.example.Proyecto_Final_Progra3.freelance.cliente;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="cliente_particular")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cliente_particular_id")
    @EqualsAndHashCode.Include
    private Integer clienteId;

    @Column(name="nombre", nullable = false, length = 50)
    private String firstName;

    @Column(name="apellido", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name="telefono", length = 30)
    private String phoneNumber;

    @Column(name="pais", length = 50)
    private String country;

    @Column(name="fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perfil_id", unique = true)
    private PerfilEntity perfil;
}
