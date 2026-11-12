package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.dto.PerfilUsuarioDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BadRequestException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.model.PerfilEntity;
import com.example.Proyecto_Final_Progra3.model.ProyectoPerfilEntity;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.PortfolioEntity;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
import com.example.Proyecto_Final_Progra3.repository.ProyectoPerfilRepository;
import com.example.Proyecto_Final_Progra3.repository.enterprise.PortfolioRepository;
import com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces.IPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioServiceImpl implements IPortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final EmployeeRepository employeeRepository;
    private final ProyectoPerfilRepository proyectoPerfilRepository;

    @Override
    @Transactional
    public void guardarCV(Integer employeeId, String username, MultipartFile file) {
        if (file == null || !"application/pdf".equals(file.getContentType())) {
            throw new BadRequestException("El archivo debe ser un formato PDF válido.");
        }
        if (file.getSize() > 5L * 1024 * 1024) {   // RF06: CV hasta 5 MB (el límite global de subida es 8 MB por el banner)
            throw new BadRequestException("El CV no puede superar los 5 MB.");
        }

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId));

        // Validación anti-IDOR: solo el titular puede subir/pisar su propio CV
        if (employee.getPerfil() == null || !employee.getPerfil().getUsuario().equals(username)) {
            throw new UnauthorizedAccessException("No tienes permiso para actualizar el currículum de otro postulante");
        }

        PortfolioEntity portfolio = portfolioRepository.findByEmployee_EmployeeId(employeeId)
                .orElseGet(() -> {
                    PortfolioEntity nuevoPortfolio = new PortfolioEntity();
                    nuevoPortfolio.setEmployee(employee);
                    nuevoPortfolio.setDescripcion("Mi Portfolio");
                    return nuevoPortfolio;
                });

        try {
            portfolio.setCv(file.getBytes());
            portfolioRepository.save(portfolio);
        } catch (IOException e) {
            throw new BusinessRuleException("Error al procesar y guardar el archivo PDF del CV.");
        }
    }

    @Override
    @Transactional
    public PerfilUsuarioDTO.ProyectoDTO addProyecto(PerfilEntity perfil, String nombre, String descripcion, String link, MultipartFile imagen) throws IOException {
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
    public void removeProyecto(PerfilEntity perfil, Integer proyectoId) {
        ProyectoPerfilEntity proyecto = proyectoPerfilRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + proyectoId));

        if (!proyecto.getPerfil().getId().equals(perfil.getId())) {
            throw new UnauthorizedAccessException("No tiene permisos para eliminar este proyecto.");
        }

        proyectoPerfilRepository.delete(proyecto);
    }
}
