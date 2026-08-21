package com.example.Proyecto_Final_Progra3.dto.responsedto;

import com.example.Proyecto_Final_Progra3.model.enterprise.EstadoPostulacion;
import java.time.LocalDate;

public record PostulacionResponseDTO(
        Integer postulacionId,
        Integer vacanteId,
        String puestoVacante,
        Integer employeeId,
        String nombreCandidato,
        String emailCandidato,
        LocalDate fechaPostulacion,
        EstadoPostulacion estado,
        String mensaje,
        boolean tieneCvSubido
) {}
