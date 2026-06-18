package com.example.Proyecto_Final_Progra3.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Perfil")
public class Perfil implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfil_id")
    private Integer id;

    @Column(name = "usuario", nullable = false, unique = true)
    private String usuario;

    @Column(nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    private boolean is_active = true;

    @Lob
    @Column(name = "foto_perfil", columnDefinition = "LONGBLOB")
    private byte[] fotoPerfil;

    @Lob
    @Column(name = "banner_perfil", columnDefinition = "LONGBLOB")
    private byte[] bannerPerfil;

    @Column(name = "ubicacion", length = 150)
    private String ubicacion;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @OneToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
    }

}
