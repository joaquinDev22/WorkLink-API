package com.example.Proyecto_Final_Progra3.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="Perfil")
public class Perfil implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="perfil_id")
    private Integer id;

    @Column(name ="usuario")
    private String usuario;

    @Column(name="password")
    private String password;

    private boolean is_active = true;

    @OneToOne
    @JoinColumn(name="rol_id")
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
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
    }

}
