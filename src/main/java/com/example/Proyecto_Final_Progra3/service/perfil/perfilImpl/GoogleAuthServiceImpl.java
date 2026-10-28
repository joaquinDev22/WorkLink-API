package com.example.Proyecto_Final_Progra3.service.perfil.perfilImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.GoogleLoginRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.LoginResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.RolEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.RolRepository;
import com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces.IGoogleAuthService;
import com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces.IJwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthServiceImpl implements IGoogleAuthService {

    private final PerfilRepository perfilRepository;
    private final RolRepository rolRepository;
    private final EmployeeRepository employeeRepository;
    private final IJwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Autowired
    @org.springframework.context.annotation.Lazy
    private GoogleAuthServiceImpl self;

    @Value("${google.client-id}")
    private String googleClientId;

    @Override
    public LoginResponseDTO authenticateGoogle(GoogleLoginRequestDTO request) {
        // Validación criptográfica fuera de transacción de base de datos
        GoogleIdToken.Payload payload = verifyGoogleToken(request.idToken());

        String email = payload.getEmail();
        String givenName = (String) payload.get("given_name");
        String familyName = (String) payload.get("family_name");
        if (givenName == null) givenName = "Usuario";
        if (familyName == null) familyName = "Google";

        // Transacción acotada para sincronización/búsqueda en BD
        PerfilEntity perfil = (self != null ? self : this).findOrCreatePerfil(email, givenName, familyName);

        if (!perfil.is_active()) {
            throw new UnauthorizedAccessException("El perfil de usuario se encuentra desactivado.");
        }

        String token = jwtService.generateToken(perfil);

        return new LoginResponseDTO(
                "Login con Google exitoso",
                perfil.getUsuario(),
                perfil.getRol().getNombre(),
                token
        );
    }

    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new UnauthorizedAccessException("Token de Google inválido o no reconocido para este Client ID");
            }
            return idToken.getPayload();
        } catch (UnauthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al verificar token con los servidores de Google: {}", e.getMessage());
            throw new UnauthorizedAccessException("Error al procesar la autenticación con Google: " + e.getMessage());
        }
    }

    @Transactional
    public PerfilEntity findOrCreatePerfil(String email, String givenName, String familyName) {
        Optional<PerfilEntity> existente = perfilRepository.findByEmail(email);
        if (existente.isPresent()) {
            return existente.get();
        }

        // Generar nombre de usuario único a partir del email
        String baseUsuario = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "");
        String usuario = baseUsuario;
        int count = 1;
        while (perfilRepository.existsByUsuario(usuario)) {
            usuario = baseUsuario + count++;
        }

        RolEntity rol = rolRepository.findByNombre("EMPLOYEE");
        if (rol == null) {
            rol = new RolEntity();
            rol.setNombre("EMPLOYEE");
            rol = rolRepository.save(rol);
        }

        PerfilEntity nuevoPerfil = PerfilEntity.builder()
                .usuario(usuario)
                .email(email)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .rol(rol)
                .is_active(true)
                .build();

        PerfilEntity perfilGuardado = perfilRepository.save(nuevoPerfil);

        // Crear entidad Employee asociada por defecto
        EmployeeEntity employee = new EmployeeEntity();
        employee.setNombre(givenName);
        employee.setApellido(familyName);
        employee.setEmail(email);
        employee.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        employee.setPerfil(perfilGuardado);
        employeeRepository.save(employee);

        return perfilGuardado;
    }
}

