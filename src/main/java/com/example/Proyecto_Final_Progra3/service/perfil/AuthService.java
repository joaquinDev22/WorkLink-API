package com.example.Proyecto_Final_Progra3.service.perfil;

import com.example.Proyecto_Final_Progra3.DTO.requestdto.LogginRequestDTO;
import com.example.Proyecto_Final_Progra3.DTO.requestdto.PerfilRequestDTO;
import com.example.Proyecto_Final_Progra3.DTO.responsedto.LogginResponseDTO;
import com.example.Proyecto_Final_Progra3.DTO.responsedto.PerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PerfilRepository perfilRepository;
    private  PasswordEncoder passwordEncoder;

    public PerfilResponseDTO register(PerfilRequestDTO dto) {

        Perfil perfil = new Perfil();

        perfil.setUsuario(dto.getUsuario());

        perfil.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        perfil.setRol(dto.getRol());


        if (perfilRepository.findByUsuario(dto.getUsuario()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        Perfil perfilGuardado = perfilRepository.save(perfil);

        return new PerfilResponseDTO(
                perfilGuardado.getId(),
                perfilGuardado.getUsuario(),
                perfilGuardado.getRol()
        );
    }

    public LogginResponseDTO loggin(LogginRequestDTO dto) {
        Perfil perfil = perfilRepository.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if(!passwordEncoder.matches(dto.getPassword(), perfil.getPassword())) {
            throw new RuntimeException("CONTRASEÑA INCORRECTA");
        }
        if(!perfil.is_active()){
            throw new RuntimeException("EL PERFIL ESTA DESACTIVADO");
        }

        return new LogginResponseDTO(
                "Login exitoso",
                perfil.getUsuario(),
                perfil.getRol()
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
