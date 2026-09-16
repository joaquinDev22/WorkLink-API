package com.example.Proyecto_Final_Progra3.enterprise.talento;

import com.example.Proyecto_Final_Progra3.errors.BadRequestException;
import com.example.Proyecto_Final_Progra3.errors.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.errors.ResourceNotFoundException;

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
    public void guardarCV(Integer employeeId, MultipartFile file) {
        if (file == null || !"application/pdf".equals(file.getContentType())) {
            throw new BadRequestException("El archivo debe ser un formato PDF válido.");
        }

        PortfolioEntity portfolio = portfolioRepository.findByEmployee_EmployeeId(employeeId)
                .orElseGet(() -> {
                    EmployeeEntity employee = employeeRepository.findById(employeeId)
                            .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId));
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
