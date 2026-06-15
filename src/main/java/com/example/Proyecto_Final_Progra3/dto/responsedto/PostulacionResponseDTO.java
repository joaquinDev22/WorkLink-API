package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.enterprise.EstadoPostulacion;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PostulacionResponseDTO {
    private Integer postulacionId;
    private Integer vacanteId;
    private String puestoVacante;
    private Integer employeeId;
    private String nombreCandidato;
    private String emailCandidato;
    private LocalDate fechaPostulacion;
    private EstadoPostulacion estado;
    private String mensaje;
    private boolean tieneCvSubido;
}
