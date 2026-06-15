package com.example.Proyecto_Final_Progra3.dto.responsedto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CertificadoResponseDTO {
    private Integer id;
    private String codigoCertificado;
    private LocalDate fechaEmision;
    private Integer bootcamp_id;
    private Integer employee_id;
}
