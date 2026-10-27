package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.exceptions.domain.BadRequestException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;
import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.enterprise.PortfolioEntity;
import com.example.Proyecto_Final_Progra3.repository.EmployeeRepository;
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

    @Override
    @Transactional
    public void guardarCV(Integer employeeId, String username, MultipartFile file) {
        if (file == null || !"application/pdf".equals(file.getContentType())) {
            throw new BadRequestException("El archivo debe ser un formato PDF válido.");
        }

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId));

        // Validación anti-IDOR: solo el titular puede subir/pisar su propio CV
        if (employee.getPerfil() != null && !employee.getPerfil().getUsuario().equals(username)) {
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
}
