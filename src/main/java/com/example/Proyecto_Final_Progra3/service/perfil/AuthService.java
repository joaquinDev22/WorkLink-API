package com.example.Proyecto_Final_Progra3.service.perfil;

import com.example.Proyecto_Final_Progra3.dto.requestdto.LogginRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.requestdto.PerfilRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.LogginResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.RolRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.example.Proyecto_Final_Progra3.mappers.PerfilMapper;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RolRepository rolRepository;
    private final JwtService jwtService;
    private final PerfilMapper mapper;

    @Transactional
    public PerfilResponseDTO register(PerfilRequestDTO dto) {

        if (perfilRepository.findByUsuario(dto.getUsuario()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Perfil perfil = mapper.toEntity(dto);
        perfil.setPassword(passwordEncoder.encode(dto.getPassword()));
        perfil.setRol(rolRepository.findByNombre(dto.getRol()));

        perfil = perfilRepository.save(perfil);

        return mapper.toDto(perfil);
    }

    public LogginResponseDTO loggin(LogginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsuario(),
                        dto.getPassword()
                ));

        Perfil perfil = (Perfil) authentication.getPrincipal();

        assert perfil != null;

        if(!perfil.is_active()){
            throw new RuntimeException("EL PERFIL ESTA DESACTIVADO");
        }
        String token = jwtService.generateToken(perfil);

        return new LogginResponseDTO(
                "Login exitoso",
                perfil.getUsuario(),
                perfil.getRol().getNombre(),
                token
        );
    }

    @Transactional
    public void bajaPerfil(PerfilRequestDTO dto) {
        Perfil perfil = perfilRepository.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        perfil.set_active(false);
        perfilRepository.save(perfil);
    }

    @Transactional
    public void altaPerfil(PerfilRequestDTO dto) {
        Perfil perfil = perfilRepository.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        perfil.set_active(true);
        perfilRepository.save(perfil);
    }
}
