package com.example.Proyecto_Final_Progra3.model;

import com.example.Proyecto_Final_Progra3.model.enums.Rol;
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
    private int id;

    @Column(name ="nombre_usuario")
    private String usuario;

    @Column(name="password_usuario")
    private String password;

    @Column(name="rol")
    @Enumerated(EnumType.STRING)
    private Rol rol;


    private boolean is_active = true;

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
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

}
