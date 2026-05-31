package com.example.Proyecto_Final_Progra3.service.perfil;

import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PerfilDetailService extends UserDetailsService {
    private final PerfilRepository perfilRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        return perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

}
