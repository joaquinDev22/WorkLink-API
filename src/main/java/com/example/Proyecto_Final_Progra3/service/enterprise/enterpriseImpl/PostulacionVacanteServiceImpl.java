package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BadRequestException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.mappers.PostulacionVacanteMapper;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.EstadoPostulacion;
import com.example.Proyecto_Final_Progra3.model.enterprise.PostulacionVacanteEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.VacanteEntity;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.PostulacionVacanteRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.RecruiterRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.VacanteRepository;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IPostulacionVacanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostulacionVacanteServiceImpl implements IPostulacionVacanteService {

    private final PostulacionVacanteRepository postulacionRepository;
    private final VacanteRepository vacanteRepository;
    private final EmployeeRepository employeeRepository;
    private final PerfilRepository perfilRepository;
    private final RecruiterRepository recruiterRepository;
    private final PostulacionVacanteMapper postulacionVacanteMapper;

    @Override
    @Transactional
    public PostulacionResponseDTO postularseMe(String username, Integer vacanteId, String mensaje, MultipartFile cv) {
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
                
        EmployeeEntity employee = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new UnauthorizedAccessException("Solo los empleados pueden postularse a vacantes"));
                
        return postularse(vacanteId, employee.getEmployeeId(), mensaje, cv);
    }

    @Override
    @Transactional
    public PostulacionResponseDTO postularse(Integer vacanteId, Integer employeeId, String mensaje, MultipartFile cv) {
        VacanteEntity vacante = vacanteRepository.findById(vacanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + vacanteId));

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId));

        PostulacionVacanteEntity postulacion = new PostulacionVacanteEntity();
        postulacion.setVacante(vacante);
        postulacion.setEmployee(employee);
        postulacion.setFechaPostulacion(LocalDate.now());
        postulacion.setEstado(EstadoPostulacion.PENDIENTE);
        postulacion.setMensaje(mensaje);

        if (cv != null && !cv.isEmpty()) {
            if (!"application/pdf".equals(cv.getContentType())) {
                throw new BadRequestException("El CV debe ser un archivo PDF.");
            }
            try {
                postulacion.setCv(cv.getBytes());
            } catch (IOException e) {
                throw new BusinessRuleException("Error al procesar el archivo PDF del CV.");
            }
        }

        PostulacionVacanteEntity guardada = postulacionRepository.save(postulacion);
        return postulacionVacanteMapper.toDto(guardada);
    }

    @Override
    public List<PostulacionResponseDTO> listarPostulantes(Integer vacanteId) {
        return postulacionVacanteMapper.toDtoList(postulacionRepository.findByVacante_VacanteId(vacanteId));
    }

    @Override
    @Transactional
    public PostulacionResponseDTO cambiarEstado(Integer vacanteId, Integer postulacionId, EstadoPostulacion nuevoEstado, String username) {
        PostulacionVacanteEntity postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada con ID: " + postulacionId));

        if (!postulacion.getVacante().getVacanteId().equals(vacanteId)) {
            throw new BusinessRuleException("La postulación no pertenece a la vacante indicada");
        }

        VacanteEntity vacante = postulacion.getVacante();
        PerfilEntity perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        boolean autorizado = false;
        if (vacante.getEmpresa() != null && vacante.getEmpresa().getPerfil() != null &&
            vacante.getEmpresa().getPerfil().getUsuario().equals(username)) {
            autorizado = true;
        } else if (vacante.getEmpresa() != null && recruiterRepository.findByPerfil(perfil).map(r -> r.getEmpresa() != null && r.getEmpresa().getId().equals(vacante.getEmpresa().getId())).orElse(false)) {
            autorizado = true;
        }

        if (!autorizado) {
            throw new UnauthorizedAccessException("No tienes permisos para modificar el estado de esta postulación");
        }

        postulacion.setEstado(nuevoEstado);
        PostulacionVacanteEntity guardada = postulacionRepository.save(postulacion);
        return postulacionVacanteMapper.toDto(guardada);
    }
}
