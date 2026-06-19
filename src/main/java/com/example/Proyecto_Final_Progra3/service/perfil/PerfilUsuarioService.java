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
import com.example.Proyecto_Final_Progra3.model.employee.Habilidad;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeHabilidad;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeHabilidadId;
import com.example.Proyecto_Final_Progra3.model.ProyectoPerfil;
import com.example.Proyecto_Final_Progra3.repository.employee.HabilidadRepository;
import com.example.Proyecto_Final_Progra3.repository.employee.EmployeeHabilidadRepository;
import com.example.Proyecto_Final_Progra3.repository.ProyectoPerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerfilUsuarioService {

    private final PerfilRepository perfilRepository;
    private final EmployeeRepository employeeRepository;
    private final EmpresaRepository empresaRepository;
    private final ClientRepository clientRepository;
    private final PortfolioService portfolioService;
    
    private final ProyectoPerfilRepository proyectoPerfilRepository;
    private final HabilidadRepository habilidadRepository;
    private final EmployeeHabilidadRepository employeeHabilidadRepository;

    @Transactional(readOnly = true)
    public PerfilUsuarioDTO getMyProfile(String username) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        PerfilUsuarioDTO dto = new PerfilUsuarioDTO();
        dto.setRol(perfil.getRol().getNombre());
        dto.setEmail(perfil.getEmail());
        dto.setUbicacion(perfil.getUbicacion());
        dto.setDescripcion(perfil.getDescripcion());
        dto.setFotoPerfil(perfil.getFotoPerfil());
        dto.setBannerPerfil(perfil.getBannerPerfil());
        
        String rol = perfil.getRol().getNombre().toUpperCase();
        if ("EMPLOYEE".equals(rol) || "FREELANCER".equals(rol) || "POSTULANTE".equals(rol)) {
            Employee emp = employeeRepository.findByPerfil(perfil).orElse(null);
            if (emp != null) {
                dto.setNombre(emp.getNombre());
                dto.setApellido(emp.getApellido());
                dto.setFechaNacimiento(emp.getFechaNacimiento() != null ? emp.getFechaNacimiento().toString() : null);
                
                // Map skills
                List<EmployeeHabilidad> empHabs = employeeHabilidadRepository.findByEmployee(emp);
                List<PerfilUsuarioDTO.HabilidadDTO> habs = empHabs.stream().map(eh -> {
                    PerfilUsuarioDTO.HabilidadDTO hd = new PerfilUsuarioDTO.HabilidadDTO();
                    hd.setHabilidadId(eh.getHabilidad().getHabilidadId());
                    hd.setNombre(eh.getHabilidad().getNombreHabilidad());
                    hd.setNivel(eh.getNivel());
                    return hd;
                }).collect(Collectors.toList());
                dto.setHabilidades(habs);
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
                dto.setSitioWeb(emp.getSitio_web());
            }
        } else if ("INSTRUCTOR".equals(rol)) {
            com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor inst = 
                com.example.Proyecto_Final_Progra3.service.BeanUtil.getBean(com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository.class).findByPerfil(perfil).orElse(null);
            if (inst != null) {
                dto.setNombre(inst.getNombre());
                dto.setApellido(inst.getApellido());
                dto.setTipoInstructor(inst.getTipo() != null ? inst.getTipo().name() : null);
            }
        } else if ("RECRUITER".equals(rol)) {
            com.example.Proyecto_Final_Progra3.model.enterprise.Recruiter rec = 
                com.example.Proyecto_Final_Progra3.service.BeanUtil.getBean(com.example.Proyecto_Final_Progra3.repository.enterprise.RecruiterRepository.class).findByPerfil(perfil).orElse(null);
            if (rec != null) {
                dto.setNombre(rec.getNombre());
                dto.setApellido(rec.getApellido());
                dto.setFechaNacimiento(rec.getFechaNacimiento() != null ? rec.getFechaNacimiento().toString() : null);
            }
        }
        
        // Map projects (for all profiles)
        List<ProyectoPerfil> proyectos = proyectoPerfilRepository.findByPerfilOrderByFechaCreacionDesc(perfil);
        List<PerfilUsuarioDTO.ProyectoDTO> projs = proyectos.stream().map(p -> {
            PerfilUsuarioDTO.ProyectoDTO pd = new PerfilUsuarioDTO.ProyectoDTO();
            pd.setId(p.getId());
            pd.setNombre(p.getNombre());
            pd.setDescripcion(p.getDescripcion());
            pd.setLink(p.getLink());
            pd.setImagen(p.getImagen());
            return pd;
        }).collect(Collectors.toList());
        dto.setProyectos(projs);
        
        return dto;
    }

    @Transactional
    public PerfilUsuarioDTO updateMyProfile(String username, PerfilUsuarioDTO request) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        if (request.getEmail() != null) perfil.setEmail(request.getEmail());
        if (request.getUbicacion() != null) perfil.setUbicacion(request.getUbicacion());
        if (request.getDescripcion() != null) perfil.setDescripcion(request.getDescripcion());
        
        perfilRepository.save(perfil);
        
        String rol = perfil.getRol().getNombre().toUpperCase();
        if ("EMPLOYEE".equals(rol) || "FREELANCER".equals(rol) || "POSTULANTE".equals(rol)) {
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
            if (request.getSitioWeb() != null) emp.setSitio_web(request.getSitioWeb());
            empresaRepository.save(emp);
        } else if ("INSTRUCTOR".equals(rol)) {
            com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor inst = 
                com.example.Proyecto_Final_Progra3.service.BeanUtil.getBean(com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository.class).findByPerfil(perfil).orElseGet(() -> {
                com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor i = new com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor();
                i.setPerfil(perfil);
                return i;
            });
            if (request.getNombre() != null) inst.setNombre(request.getNombre());
            if (request.getApellido() != null) inst.setApellido(request.getApellido());
            if (request.getTipoInstructor() != null) {
                try {
                    inst.setTipo(com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo.valueOf(request.getTipoInstructor().toUpperCase()));
                } catch(Exception ignored){}
            }
            com.example.Proyecto_Final_Progra3.service.BeanUtil.getBean(com.example.Proyecto_Final_Progra3.repository.bootcamp.InstructorRepository.class).save(inst);
        } else if ("RECRUITER".equals(rol)) {
            com.example.Proyecto_Final_Progra3.model.enterprise.Recruiter rec = 
                com.example.Proyecto_Final_Progra3.service.BeanUtil.getBean(com.example.Proyecto_Final_Progra3.repository.enterprise.RecruiterRepository.class).findByPerfil(perfil).orElseGet(() -> {
                com.example.Proyecto_Final_Progra3.model.enterprise.Recruiter r = new com.example.Proyecto_Final_Progra3.model.enterprise.Recruiter();
                r.setPerfil(perfil);
                return r;
            });
            if (request.getNombre() != null) rec.setNombre(request.getNombre());
            if (request.getApellido() != null) rec.setApellido(request.getApellido());
            if (request.getEmail() != null) rec.setEmail(request.getEmail());
            if (request.getFechaNacimiento() != null && !request.getFechaNacimiento().isEmpty()) {
                rec.setFechaNacimiento(LocalDate.parse(request.getFechaNacimiento()));
            }
            com.example.Proyecto_Final_Progra3.service.BeanUtil.getBean(com.example.Proyecto_Final_Progra3.repository.enterprise.RecruiterRepository.class).save(rec);
        }
        
        return getMyProfile(username);
    }
    
    @Transactional
    public void uploadFoto(String username, MultipartFile file) throws IOException {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        perfil.setFotoPerfil(file.getBytes());
        perfilRepository.save(perfil);
    }

    @Transactional
    public void uploadBanner(String username, MultipartFile file) throws IOException {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        perfil.setBannerPerfil(file.getBytes());
        perfilRepository.save(perfil);
    }

    @Transactional
    public PerfilUsuarioDTO.HabilidadDTO addHabilidad(String username, String nombreHabilidad, Integer nivel) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        Employee emp = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo los empleados pueden tener habilidades"));
                
        Habilidad habilidad = habilidadRepository.findByNombreHabilidad(nombreHabilidad)
                .orElseGet(() -> {
                    Habilidad h = new Habilidad();
                    h.setNombreHabilidad(nombreHabilidad);
                    return habilidadRepository.save(h);
                });
                
        EmployeeHabilidad empHab = new EmployeeHabilidad();
        EmployeeHabilidadId id = new EmployeeHabilidadId();
        id.setEmployeeId(emp.getEmployeeId());
        id.setHabilidadId(habilidad.getHabilidadId());
        
        empHab.setId(id);
        empHab.setEmployee(emp);
        empHab.setHabilidad(habilidad);
        empHab.setNivel(nivel);
        
        employeeHabilidadRepository.save(empHab);
        
        PerfilUsuarioDTO.HabilidadDTO dto = new PerfilUsuarioDTO.HabilidadDTO();
        dto.setHabilidadId(habilidad.getHabilidadId());
        dto.setNombre(habilidad.getNombreHabilidad());
        dto.setNivel(nivel);
        return dto;
    }

    @Transactional
    public void removeHabilidad(String username, Integer habilidadId) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        Employee emp = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo los empleados pueden tener habilidades"));
                
        EmployeeHabilidadId id = new EmployeeHabilidadId();
        id.setEmployeeId(emp.getEmployeeId());
        id.setHabilidadId(habilidadId);
        
        employeeHabilidadRepository.deleteById(id);
    }

    @Transactional
    public PerfilUsuarioDTO.ProyectoDTO addProyecto(String username, String nombre, String descripcion, String link, MultipartFile imagen) throws IOException {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        long count = proyectoPerfilRepository.countByPerfil(perfil);
        if (count >= 5) {
            throw new RuntimeException("Solo se permiten hasta 5 proyectos en el perfil.");
        }
                
        ProyectoPerfil proyecto = new ProyectoPerfil();
        proyecto.setPerfil(perfil);
        proyecto.setNombre(nombre);
        proyecto.setDescripcion(descripcion);
        proyecto.setLink(link);
        if (imagen != null && !imagen.isEmpty()) {
            proyecto.setImagen(imagen.getBytes());
        }
        
        proyecto = proyectoPerfilRepository.save(proyecto);
        
        PerfilUsuarioDTO.ProyectoDTO dto = new PerfilUsuarioDTO.ProyectoDTO();
        dto.setId(proyecto.getId());
        dto.setNombre(proyecto.getNombre());
        dto.setDescripcion(proyecto.getDescripcion());
        dto.setLink(proyecto.getLink());
        dto.setImagen(proyecto.getImagen());
        return dto;
    }

    @Transactional
    public void removeProyecto(String username, Integer proyectoId) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        ProyectoPerfil proyecto = proyectoPerfilRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
                
        if (!proyecto.getPerfil().getId().equals(perfil.getId())) {
            throw new RuntimeException("No tiene permisos para eliminar este proyecto.");
        }
        
        proyectoPerfilRepository.delete(proyecto);
    }

    @Transactional
    public void uploadMyCV(String username, MultipartFile file) {
        Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        String rol = perfil.getRol().getNombre().toUpperCase();
        if (!"EMPLOYEE".equals(rol) && !"FREELANCER".equals(rol) && !"POSTULANTE".equals(rol)) {
            throw new RuntimeException("Solo los freelancers/empleados pueden subir CV.");
        }
        
        Employee emp = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("No se encontró el registro de empleado."));
                
        portfolioService.guardarCV(emp.getEmployeeId(), file);
    }
}
