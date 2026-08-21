package com.example.Proyecto_Final_Progra3.service.perfil.perfilImpl;

import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces.IPerfilDetailService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PerfilDetailServiceImpl implements IPerfilDetailService {
    private final PerfilRepository perfilRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
