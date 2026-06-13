package com.example.Proyecto_Final_Progra3.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CertificadoBootcampRequestDTO {
    private Integer certificadoId;
    private String codigoCertificado;
    private LocalDate fechaEmision;

    private String nombreEmpleado;
    private String tituloBootcamp;

    private String mensaje;
}
