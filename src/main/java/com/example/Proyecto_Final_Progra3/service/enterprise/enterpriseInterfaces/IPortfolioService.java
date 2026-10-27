package com.example.Proyecto_Final_Progra3.service.enterprise.enterpriseInterfaces;

import org.springframework.web.multipart.MultipartFile;

public interface IPortfolioService {
    void guardarCV(Integer employeeId, String username, MultipartFile file);
}
