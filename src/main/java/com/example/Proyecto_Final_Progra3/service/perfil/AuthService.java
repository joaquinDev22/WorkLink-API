package com.example.Proyecto_Final_Progra3.service.perfil;

import com.example.Proyecto_Final_Progra3.dto.requestdto.LogginRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.requestdto.PerfilRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.LogginResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.RolRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PerfilRepository perfilRepository;
    private final  PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RolRepository rolRepository;
    private final JwtService jwtService;

    public AuthService(PerfilRepository perfilRepository, PasswordEncoder passwordEncoder, RolRepository rolRepository, JwtService jwtService,  AuthenticationManager authenticationManager) {
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public PerfilResponseDTO register(PerfilRequestDTO dto) {

        if (perfilRepository.findByUsuario(dto.getUsuario()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Perfil perfil = new Perfil();

        perfil.setUsuario(dto.getUsuario());

        perfil.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        perfil.setRol(rolRepository.findByNombre(dto.getRol()));



        Perfil perfilGuardado = perfilRepository.save(perfil);

        return new PerfilResponseDTO(
                perfilGuardado.getId(),
                perfilGuardado.getUsuario(),
                perfilGuardado.getRol().getNombre()
        );
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

    public void bajaPerfil(PerfilRequestDTO dto) {
        Perfil perfil = perfilRepository.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        perfil.set_active(false);
        perfilRepository.save(perfil);
    }

    public void altaPerfil(PerfilRequestDTO dto) {
        Perfil perfil = perfilRepository.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        perfil.set_active(true);
        perfilRepository.save(perfil);
    }


}
