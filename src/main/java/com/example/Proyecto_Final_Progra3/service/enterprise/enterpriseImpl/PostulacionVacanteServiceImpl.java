package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionResponseDTO;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.enterprise.EstadoPostulacion;
import com.example.Proyecto_Final_Progra3.model.enterprise.PostulacionVacante;
import com.example.Proyecto_Final_Progra3.model.enterprise.Vacante;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.PerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.PostulacionVacanteRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.VacanteRepository;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IPostulacionVacanteService;
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
public class PostulacionVacanteServiceImpl implements IPostulacionVacanteService {

    private final PostulacionVacanteRepository postulacionRepository;
    private final VacanteRepository vacanteRepository;
    private final EmployeeRepository employeeRepository;
    private final PerfilRepository perfilRepository;

    @Override
    @Transactional
    public PostulacionResponseDTO postularseMe(String username, Integer vacanteId, String mensaje, MultipartFile cv) {
        com.example.Proyecto_Final_Progra3.model.Perfil perfil = perfilRepository.findByUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        Employee employee = employeeRepository.findByPerfil(perfil)
                .orElseThrow(() -> new RuntimeException("Solo los empleados pueden postularse a vacantes"));
                
        return postularse(vacanteId, employee.getEmployeeId(), mensaje, cv);
    }

    @Override
    @Transactional
    public PostulacionResponseDTO postularse(Integer vacanteId, Integer employeeId, String mensaje, MultipartFile cv) {
        Vacante vacante = vacanteRepository.findById(vacanteId)
                .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        PostulacionVacante postulacion = new PostulacionVacante();
        postulacion.setVacante(vacante);
        postulacion.setEmployee(employee);
        postulacion.setFechaPostulacion(LocalDate.now());
        postulacion.setEstado(EstadoPostulacion.PENDIENTE);
        postulacion.setMensaje(mensaje);

        if (cv != null && !cv.isEmpty()) {
            if (!"application/pdf".equals(cv.getContentType())) {
                throw new RuntimeException("El CV debe ser un archivo PDF.");
            }
            try {
                postulacion.setCv(cv.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar el archivo PDF del CV.");
            }
        }

        PostulacionVacante guardada = postulacionRepository.save(postulacion);
        return mapToDto(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostulacionResponseDTO> listarPostulantes(Integer vacanteId) {
        return postulacionRepository.findByVacante_VacanteId(vacanteId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PostulacionResponseDTO mapToDto(PostulacionVacante p) {
        return new PostulacionResponseDTO(
                p.getPostulacionVacanteId(),
                p.getVacante().getVacanteId(),
                p.getVacante().getPuesto(),
                p.getEmployee().getEmployeeId(),
                p.getEmployee().getNombre() + " " + p.getEmployee().getApellido(),
                p.getEmployee().getEmail(),
                p.getFechaPostulacion(),
                p.getEstado(),
                p.getMensaje(),
                p.getCv() != null && p.getCv().length > 0
        );
    }
}
