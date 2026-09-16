package com.example.Proyecto_Final_Progra3.enterprise.talento;

import org.springframework.web.multipart.MultipartFile;

public interface IPortfolioService {
    void guardarCV(Integer employeeId, MultipartFile file);
}
