package com.example.Proyecto_Final_Progra3.service.perfil.perfilImpl;

import com.example.Proyecto_Final_Progra3.dto.requestdto.LoginRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.requestdto.PerfilRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.LoginResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PerfilResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.mappers.PerfilMapper;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.RolEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.InstructorEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.EmpresaEntity;
import com.example.Proyecto_Final_Progra3.model.freelance.ClientEntity;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.RolRepository;
import com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ClientRepository;
import com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces.IAuthService;
import com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces.IJwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements IAuthService {

    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RolRepository rolRepository;
    private final IJwtService jwtService;
    private final PerfilMapper mapper;

    private final EmployeeRepository employeeRepository;
    private final EmpresaRepository empresaRepository;
    private final ClientRepository clientRepository;
    private final InstructorRepository instructorRepository;

    @Override
    @Transactional
    public PerfilResponseDTO register(PerfilRequestDTO dto) {

        if (perfilRepository.findByUsuario(dto.usuario()).isPresent()) {
            throw new BusinessRuleException("El nombre de usuario ya se encuentra registrado");
        }

        PerfilEntity perfil = mapper.toEntity(dto);
        perfil.setPassword(passwordEncoder.encode(dto.password()));

        RolEntity rol = rolRepository.findByNombre(dto.rol().toUpperCase());
        if (rol == null) {
            rol = new RolEntity();
            rol.setNombre(dto.rol().toUpperCase());
            rolRepository.save(rol);
        }
        perfil.setRol(rol);

        perfil = perfilRepository.save(perfil);

        String rolName = perfil.getRol().getNombre().toUpperCase();
        if ("EMPLOYEE".equals(rolName) || "FREELANCER".equals(rolName) || "POSTULANTE".equals(rolName)) {
            EmployeeEntity employee = new EmployeeEntity();
            employee.setNombre(perfil.getUsuario());
            employee.setApellido("");
            employee.setEmail(perfil.getEmail() != null ? perfil.getEmail() : "");
            employee.setFechaNacimiento(java.time.LocalDate.of(2000, 1, 1));
            employee.setPerfil(perfil);
            employeeRepository.save(employee);
        } else if ("CLIENTE".equals(rolName)) {
            ClientEntity client = new ClientEntity();
            client.setFirstName(perfil.getUsuario());
            client.setLastName("");
            client.setEmail(perfil.getEmail() != null ? perfil.getEmail() : "");
            client.setFechaNacimiento(java.time.LocalDate.of(2000, 1, 1));
            client.setCountry("");
            client.setPhoneNumber("");
            client.setPerfil(perfil);
            clientRepository.save(client);
        } else if ("EMPRESA".equals(rolName)) {
            EmpresaEntity empresa = new EmpresaEntity();
            empresa.setNombre(perfil.getUsuario());
            empresa.setDescripcion("");
            empresa.setSitio_web("");
            empresa.setUbicacion("");
            empresa.setPerfil(perfil);
            empresaRepository.save(empresa);
        } else if ("INSTRUCTOR".equals(rolName)) {
            InstructorEntity instructor = new InstructorEntity();
            instructor.setNombre(perfil.getUsuario());
            instructor.setApellido("");
            instructor.setPresentacion("");
            instructor.setTitulo("");
            instructor.setTipo(com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo.PARTICULAR);
            instructor.setPerfil(perfil);
            instructorRepository.save(instructor);
        }

        return mapper.toDto(perfil);
    }

    @Override
    public LoginResponseDTO loggin(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.usuario(),
                        dto.password()));

        PerfilEntity perfil = (PerfilEntity) authentication.getPrincipal();

        if (perfil == null) {
            throw new UnauthorizedAccessException("Autenticación fallida: perfil nulo");
        }

        if (!perfil.is_active()) {
            throw new UnauthorizedAccessException("El perfil de usuario se encuentra desactivado.");
        }
        String token = jwtService.generateToken(perfil);

        return new LoginResponseDTO(
                "Login exitoso",
                perfil.getUsuario(),
                perfil.getRol().getNombre(),
                token);
    }

    @Override
    @Transactional
    public void bajaPerfil(PerfilRequestDTO dto) {
        PerfilEntity perfil = perfilRepository.findByUsuario(dto.usuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + dto.usuario()));

        perfil.set_active(false);
        perfilRepository.save(perfil);
    }

    @Override
    @Transactional
    public void altaPerfil(PerfilRequestDTO dto) {
        PerfilEntity perfil = perfilRepository.findByUsuario(dto.usuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + dto.usuario()));

        perfil.set_active(true);
        perfilRepository.save(perfil);
    }
}
