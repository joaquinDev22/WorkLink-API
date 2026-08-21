package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseImpl;

import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import com.example.Proyecto_Final_Progra3.model.enterprise.Portfolio;
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
public class PortfolioServiceImpl implements IPortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void guardarCV(Integer employeeId, MultipartFile file) {
        if (!"application/pdf".equals(file.getContentType())) {
            throw new RuntimeException("El archivo debe ser un formato PDF válido.");
        }

        Portfolio portfolio = portfolioRepository.findByEmployee_EmployeeId(employeeId)
                .orElseGet(() -> {
                    Employee employee = employeeRepository.findById(employeeId)
                            .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));
                    Portfolio nuevoPortfolio = new Portfolio();
                    nuevoPortfolio.setEmployee(employee);
                    nuevoPortfolio.setDescripcion("Mi Portfolio");
                    return nuevoPortfolio;
                });

        try {
            portfolio.setCv(file.getBytes());
            portfolioRepository.save(portfolio);
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar y guardar el archivo PDF.", e);
        }
    }
}
