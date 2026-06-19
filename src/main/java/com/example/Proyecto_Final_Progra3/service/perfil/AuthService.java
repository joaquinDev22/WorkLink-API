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
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.freelance.Client;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ClientRepository;

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

    private final EmployeeRepository employeeRepository;
    private final EmpresaRepository empresaRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public PerfilResponseDTO register(PerfilRequestDTO dto) {

        if (perfilRepository.findByUsuario(dto.getUsuario()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Perfil perfil = mapper.toEntity(dto);
        perfil.setPassword(passwordEncoder.encode(dto.getPassword()));

        com.example.Proyecto_Final_Progra3.model.Rol rol = rolRepository.findByNombre(dto.getRol().toUpperCase());
        if (rol == null) {
            rol = new com.example.Proyecto_Final_Progra3.model.Rol();
            rol.setNombre(dto.getRol().toUpperCase());
            rolRepository.save(rol);
        }
        perfil.setRol(rol);

        perfil = perfilRepository.save(perfil);

        String rolName = perfil.getRol().getNombre().toUpperCase();
        if ("EMPLOYEE".equals(rolName) || "FREELANCER".equals(rolName) || "POSTULANTE".equals(rolName)) {
            Employee employee = new Employee();
            employee.setNombre(perfil.getUsuario());
            employee.setApellido("");
            employee.setEmail(perfil.getEmail() != null ? perfil.getEmail() : "");
            employee.setFechaNacimiento(java.time.LocalDate.of(2000, 1, 1));
            employee.setPerfil(perfil);
            employeeRepository.save(employee);
        } else if ("CLIENTE".equals(rolName)) {
            Client client = new Client();
            client.setFirstName(perfil.getUsuario());
            client.setLastName("");
            client.setEmail(perfil.getEmail() != null ? perfil.getEmail() : "");
            client.setFechaNacimiento(java.time.LocalDate.of(2000, 1, 1));
            client.setCountry("");
            client.setPhoneNumber("");
            client.setPerfil(perfil);
            clientRepository.save(client);
        } else if ("EMPRESA".equals(rolName)) {
            Empresa empresa = new Empresa();
            empresa.setNombre(perfil.getUsuario());
            empresa.setDescripcion("");
            empresa.setSitio_web("");
            empresa.setUbicacion("");
            empresa.setPerfil(perfil);
            empresaRepository.save(empresa);
        } else if ("INSTRUCTOR".equals(rolName)) {
            com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor instructor = new com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor();
            instructor.setNombre(perfil.getUsuario());
            instructor.setApellido("");
            instructor.setPresentacion("");
            instructor.setTitulo("");
            instructor.setTipo(com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo.PARTICULAR);
            instructor.setPerfil(perfil);
            com.example.Proyecto_Final_Progra3.service.BeanUtil
                    .getBean(com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository.class)
                    .save(instructor);
        }

        return mapper.toDto(perfil);
    }

    public LogginResponseDTO loggin(LogginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsuario(),
                        dto.getPassword()));

        Perfil perfil = (Perfil) authentication.getPrincipal();

        assert perfil != null;

        if (!perfil.is_active()) {
            throw new RuntimeException("EL PERFIL ESTA DESACTIVADO");
        }
        String token = jwtService.generateToken(perfil);

        return new LogginResponseDTO(
                "Login exitoso",
                perfil.getUsuario(),
                perfil.getRol().getNombre(),
                token);
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
