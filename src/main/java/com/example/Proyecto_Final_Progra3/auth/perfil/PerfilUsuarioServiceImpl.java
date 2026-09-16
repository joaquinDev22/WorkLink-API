package com.example.Proyecto_Final_Progra3.auth.perfil;

import com.example.Proyecto_Final_Progra3.bootcamps.bootcamp.Tipo;
import com.example.Proyecto_Final_Progra3.errors.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.errors.UnauthorizedAccessException;

import com.example.Proyecto_Final_Progra3.freelance.proyecto.ProyectoPerfilEntity;
import com.example.Proyecto_Final_Progra3.bootcamps.instructor.InstructorEntity;
import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeHabilidadEntity;
import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeHabilidadId;
import com.example.Proyecto_Final_Progra3.enterprise.talento.HabilidadEntity;
import com.example.Proyecto_Final_Progra3.enterprise.empresa.EmpresaEntity;
import com.example.Proyecto_Final_Progra3.enterprise.empresa.RecruiterEntity;
import com.example.Proyecto_Final_Progra3.freelance.cliente.ClientEntity;
import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeRepository;

import com.example.Proyecto_Final_Progra3.freelance.proyecto.ProyectoPerfilRepository;
import com.example.Proyecto_Final_Progra3.bootcamps.instructor.InstructorRepository;
import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeHabilidadRepository;
import com.example.Proyecto_Final_Progra3.enterprise.talento.HabilidadRepository;
import com.example.Proyecto_Final_Progra3.enterprise.empresa.EmpresaRepository;
import com.example.Proyecto_Final_Progra3.enterprise.empresa.RecruiterRepository;
import com.example.Proyecto_Final_Progra3.freelance.cliente.ClientRepository;
import com.example.Proyecto_Final_Progra3.enterprise.talento.IPortfolioService;

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
@Transactional(readOnly = true)
public class PerfilUsuarioServiceImpl implements IPerfilUsuarioService {

    private final PerfilRepository perfilRepository;
    private final EmployeeRepository employeeRepository;
    private final EmpresaRepository empresaRepository;
    private final ClientRepository clientRepository;
    private final InstructorRepository instructorRepository;
    private final RecruiterRepository recruiterRepository;
    private final IPortfolioService portfolioService;
    
    private final ProyectoPerfilRepository proyectoPerfilRepository;
    private final HabilidadRepository habilidadRepository;
    private final EmployeeHabilidadRepository employeeHabilidadRepository;

    @Override
    public PerfilUsuarioDTO getMyProfile(String username) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        PerfilUsuarioDTO dto = new PerfilUsuarioDTO();
        dto.setRol(perfil.getRol().getNombre());
        dto.setEmail(perfil.getEmail());
        dto.setUbicacion(perfil.getUbicacion());
        dto.setDescripcion(perfil.getDescripcion());
        dto.setFotoPerfil(perfil.getFotoPerfil());
        dto.setBannerPerfil(perfil.getBannerPerfil());
        
        String rol = perfil.getRol().getNombre().toUpperCase();
        if ("EMPLOYEE".equals(rol) || "FREELANCER".equals(rol) || "POSTULANTE".equals(rol)) {
            EmployeeEntity emp = employeeRepository.findByPerfil(perfil).orElse(null);
            if (emp != null) {
                dto.setNombre(emp.getNombre());
                dto.setApellido(emp.getApellido());
                dto.setFechaNacimiento(emp.getFechaNacimiento() != null ? emp.getFechaNacimiento().toString() : null);
                
                List<EmployeeHabilidadEntity> empHabs = employeeHabilidadRepository.findByEmployee(emp);
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
            ClientEntity client = clientRepository.findByPerfil(perfil).orElse(null);
            if (client != null) {
                dto.setNombre(client.getFirstName());
                dto.setApellido(client.getLastName());
                dto.setTelefono(client.getPhoneNumber());
                dto.setPais(client.getCountry());
            }
        } else if ("EMPRESA".equals(rol)) {
            EmpresaEntity emp = empresaRepository.findByPerfil(perfil).orElse(null);
            if (emp != null) {
                dto.setNombre(emp.getNombre());
                dto.setSitioWeb(emp.getSitio_web());
            }
        } else if ("INSTRUCTOR".equals(rol)) {
            InstructorEntity inst = instructorRepository.findByPerfil(perfil).orElse(null);
            if (inst != null) {
                dto.setNombre(inst.getNombre());
                dto.setApellido(inst.getApellido());
                dto.setTipoInstructor(inst.getTipo() != null ? inst.getTipo().name() : null);
            }
        } else if ("RECRUITER".equals(rol)) {
            RecruiterEntity rec = recruiterRepository.findByPerfil(perfil).orElse(null);
            if (rec != null) {
                dto.setNombre(rec.getNombre());
                dto.setApellido(rec.getApellido());
                dto.setFechaNacimiento(rec.getFechaNacimiento() != null ? rec.getFechaNacimiento().toString() : null);
            }
        }
        
        List<ProyectoPerfilEntity> proyectos = proyectoPerfilRepository.findByPerfilOrderByFechaCreacionDesc(perfil);
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

    @Override
    @Transactional
    public PerfilUsuarioDTO updateMyProfile(String username, PerfilUsuarioDTO request) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        if (request.getEmail() != null) perfil.setEmail(request.getEmail());
        if (request.getUbicacion() != null) perfil.setUbicacion(request.getUbicacion());
        if (request.getDescripcion() != null) perfil.setDescripcion(request.getDescripcion());
        
        perfilRepository.save(perfil);
        
        String rol = perfil.getRol().getNombre().toUpperCase();
        if ("EMPLOYEE".equals(rol) || "FREELANCER".equals(rol) || "POSTULANTE".equals(rol)) {
            EmployeeEntity emp = employeeRepository.findByPerfil(perfil).orElseGet(() -> {
                EmployeeEntity e = new EmployeeEntity();
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
            ClientEntity client = clientRepository.findByPerfil(perfil).orElseGet(() -> {
                ClientEntity c = new ClientEntity();
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
            EmpresaEntity emp = empresaRepository.findByPerfil(perfil).orElseGet(() -> {
                EmpresaEntity e = new EmpresaEntity();
                e.setPerfil(perfil);
                return e;
            });
            if (request.getNombre() != null) emp.setNombre(request.getNombre());
            if (request.getSitioWeb() != null) emp.setSitio_web(request.getSitioWeb());
            empresaRepository.save(emp);
        } else if ("INSTRUCTOR".equals(rol)) {
            InstructorEntity inst = instructorRepository.findByPerfil(perfil).orElseGet(() -> {
                InstructorEntity i = new InstructorEntity();
                i.setPerfil(perfil);
                return i;
            });
            if (request.getNombre() != null) inst.setNombre(request.getNombre());
            if (request.getApellido() != null) inst.setApellido(request.getApellido());
            if (request.getTipoInstructor() != null) {
                try {
                    inst.setTipo(com.example.Proyecto_Final_Progra3.bootcamps.bootcamp.Tipo.valueOf(request.getTipoInstructor().toUpperCase()));
                } catch(Exception ignored){}
            }
            instructorRepository.save(inst);
        } else if ("RECRUITER".equals(rol)) {
            RecruiterEntity rec = recruiterRepository.findByPerfil(perfil).orElseGet(() -> {
                RecruiterEntity r = new RecruiterEntity();
                r.setPerfil(perfil);
                return r;
            });
            if (request.getNombre() != null) rec.setNombre(request.getNombre());
            if (request.getApellido() != null) rec.setApellido(request.getApellido());
            if (request.getEmail() != null) rec.setEmail(request.getEmail());
            if (request.getFechaNacimiento() != null && !request.getFechaNacimiento().isEmpty()) {
                rec.setFechaNacimiento(LocalDate.parse(request.getFechaNacimiento()));
            }
            recruiterRepository.save(rec);
        }
        
        return getMyProfile(username);
    }
    
    @Override
    @Transactional
    public void uploadFoto(String username, MultipartFile file) throws IOException {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
        perfil.setFotoPerfil(file.getBytes());
        perfilRepository.save(perfil);
    }

    @Override
    @Transactional
    public void uploadBanner(String username, MultipartFile file) throws IOException {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
        perfil.setBannerPerfil(file.getBytes());
        perfilRepository.save(perfil);
    }

    @Override
    @Transactional
    public PerfilUsuarioDTO.HabilidadDTO addHabilidad(String username, String nombreHabilidad, Integer nivel) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        EmployeeEntity emp = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("Solo los empleados pueden tener habilidades"));
                
        HabilidadEntity habilidad = habilidadRepository.findByNombreHabilidad(nombreHabilidad)
                .orElseGet(() -> {
                    HabilidadEntity h = new HabilidadEntity();
                    h.setNombreHabilidad(nombreHabilidad);
                    return habilidadRepository.save(h);
                });
                
        EmployeeHabilidadEntity empHab = new EmployeeHabilidadEntity();
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

    @Override
    @Transactional
    public void removeHabilidad(String username, Integer habilidadId) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        EmployeeEntity emp = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("Solo los empleados pueden tener habilidades"));
                
        EmployeeHabilidadId id = new EmployeeHabilidadId();
        id.setEmployeeId(emp.getEmployeeId());
        id.setHabilidadId(habilidadId);
        
        employeeHabilidadRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PerfilUsuarioDTO.ProyectoDTO addProyecto(String username, String nombre, String descripcion, String link, MultipartFile imagen) throws IOException {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        long count = proyectoPerfilRepository.countByPerfil(perfil);
        if (count >= 5) {
            throw new BusinessRuleException("Solo se permiten hasta 5 proyectos en el perfil.");
        }
                
        ProyectoPerfilEntity proyecto = new ProyectoPerfilEntity();
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

    @Override
    @Transactional
    public void removeProyecto(String username, Integer proyectoId) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        ProyectoPerfilEntity proyecto = proyectoPerfilRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));
                
        if (!proyecto.getPerfil().getId().equals(perfil.getId())) {
            throw new UnauthorizedAccessException("No tiene permisos para eliminar este proyecto.");
        }
        
        proyectoPerfilRepository.delete(proyecto);
    }

    @Override
    @Transactional
    public void uploadMyCV(String username, MultipartFile file) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        String rol = perfil.getRol().getNombre().toUpperCase();
        if (!"EMPLOYEE".equals(rol) && !"FREELANCER".equals(rol) && !"POSTULANTE".equals(rol)) {
            throw new UnauthorizedAccessException("Solo los freelancers/empleados pueden subir CV.");
        }
        
        EmployeeEntity emp = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el registro de empleado."));
                
        portfolioService.guardarCV(emp.getEmployeeId(), file);
    }
}
