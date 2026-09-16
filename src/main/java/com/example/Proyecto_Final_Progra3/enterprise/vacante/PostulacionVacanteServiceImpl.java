package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import com.example.Proyecto_Final_Progra3.errors.BadRequestException;
import com.example.Proyecto_Final_Progra3.errors.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.errors.UnauthorizedAccessException;

import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilEntity;
import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeEntity;

import com.example.Proyecto_Final_Progra3.enterprise.talento.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.auth.perfil.PerfilRepository;

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
}
