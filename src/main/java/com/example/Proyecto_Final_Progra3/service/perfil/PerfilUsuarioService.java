package com.example.Proyecto_Final_Progra3.service.perfil;

import com.example.Proyecto_Final_Progra3.dto.PerfilUsuarioDTO;
import com.example.Proyecto_Final_Progra3.model.Perfil;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import com.example.Proyecto_Final_Progra3.model.freelance.Client;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.repository.freelance.ClientRepository;
import com.example.Proyecto_Final_Progra3.service.enterprise.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PerfilUsuarioService {

    private final PerfilRepository perfilRepository;
    private final EmployeeRepository employeeRepository;
    private final EmpresaRepository empresaRepository;
    private final ClientRepository clientRepository;
    private final PortfolioService portfolioService;

    @Transactional(readOnly = true)
    public PerfilUsuarioDTO getMyProfile(String username) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        PerfilUsuarioDTO dto = new PerfilUsuarioDTO();
        dto.setRol(perfil.getRol().getNombre());
        dto.setEmail(perfil.getEmail());
        
        String rol = perfil.getRol().getNombre().toUpperCase();
        if ("EMPLOYEE".equals(rol)) {
            Employee emp = employeeRepository.findByPerfil(perfil).orElse(null);
            if (emp != null) {
                dto.setNombre(emp.getNombre());
                dto.setApellido(emp.getApellido());
                dto.setFechaNacimiento(emp.getFechaNacimiento() != null ? emp.getFechaNacimiento().toString() : null);
            }
        } else if ("CLIENTE".equals(rol)) {
            Client client = clientRepository.findByPerfil(perfil).orElse(null);
            if (client != null) {
                dto.setNombre(client.getFirstName());
                dto.setApellido(client.getLastName());
                dto.setTelefono(client.getPhoneNumber());
                dto.setPais(client.getCountry());
            }
        } else if ("EMPRESA".equals(rol)) {
            Empresa emp = empresaRepository.findByPerfil(perfil).orElse(null);
            if (emp != null) {
                dto.setNombre(emp.getNombre());
                dto.setDescripcion(emp.getDescripcion());
                dto.setUbicacion(emp.getUbicacion());
                dto.setSitioWeb(emp.getSitio_web());
            }
        }
        return dto;
    }

    @Transactional
    public PerfilUsuarioDTO updateMyProfile(String username, PerfilUsuarioDTO request) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        if (request.getEmail() != null) {
            perfil.setEmail(request.getEmail());
            perfilRepository.save(perfil);
        }
        
        String rol = perfil.getRol().getNombre().toUpperCase();
        if ("EMPLOYEE".equals(rol)) {
            Employee emp = employeeRepository.findByPerfil(perfil).orElseGet(() -> {
                Employee e = new Employee();
                e.setPerfil(perfil);
                return e;
            });
            if (request.getNombre() != null) emp.setNombre(request.getNombre());
            if (request.getApellido() != null) emp.setApellido(request.getApellido());
            if (request.getFechaNacimiento() != null && !request.getFechaNacimiento().isEmpty()) {
                emp.setFechaNacimiento(LocalDate.parse(request.getFechaNacimiento()));
            }
            if (request.getEmail() != null) emp.setEmail(request.getEmail());
            employeeRepository.save(emp);
        } else if ("CLIENTE".equals(rol)) {
            Client client = clientRepository.findByPerfil(perfil).orElseGet(() -> {
                Client c = new Client();
                c.setPerfil(perfil);
                return c;
            });
            if (request.getNombre() != null) client.setFirstName(request.getNombre());
            if (request.getApellido() != null) client.setLastName(request.getApellido());
            if (request.getTelefono() != null) client.setPhoneNumber(request.getTelefono());
            if (request.getPais() != null) client.setCountry(request.getPais());
            if (request.getEmail() != null) client.setEmail(request.getEmail());
            clientRepository.save(client);
        } else if ("EMPRESA".equals(rol)) {
            Empresa emp = empresaRepository.findByPerfil(perfil).orElseGet(() -> {
                Empresa e = new Empresa();
                e.setPerfil(perfil);
                return e;
            });
            if (request.getNombre() != null) emp.setNombre(request.getNombre());
            if (request.getDescripcion() != null) emp.setDescripcion(request.getDescripcion());
            if (request.getUbicacion() != null) emp.setUbicacion(request.getUbicacion());
            if (request.getSitioWeb() != null) emp.setSitio_web(request.getSitioWeb());
            empresaRepository.save(emp);
        }
        
        return getMyProfile(username);
    }
    
    @Transactional
    public void uploadMyCV(String username, MultipartFile file) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        String rol = perfil.getRol().getNombre().toUpperCase();
        if (!"EMPLOYEE".equals(rol)) {
            throw new RuntimeException("Solo los freelancers/empleados pueden subir CV.");
        }
        
        Employee emp = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("No se encontró el registro de empleado."));
                
        portfolioService.guardarCV(emp.getEmployeeId(), file);
    }
}
