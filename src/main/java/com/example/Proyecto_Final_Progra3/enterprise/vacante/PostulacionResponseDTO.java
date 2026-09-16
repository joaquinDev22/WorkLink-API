package com.example.Proyecto_Final_Progra3.enterprise.vacante;

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
