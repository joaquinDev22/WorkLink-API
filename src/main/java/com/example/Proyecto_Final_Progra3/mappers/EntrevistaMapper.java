package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.responsedto.EntrevistaResponseDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.EntrevistaEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class EntrevistaMapper {

    public EntrevistaResponseDTO toDto(EntrevistaEntity entity) {
        if (entity == null) {
            return null;
        }
        var postulacion = entity.getPostulacion();
        var employee = postulacion != null ? postulacion.getEmployee() : null;
        var vacante = postulacion != null ? postulacion.getVacante() : null;

        String candidateName = employee != null
                ? (employee.getNombre() + (employee.getApellido() != null ? " " + employee.getApellido() : "")).trim()
                : "Candidato";
        String candidateEmail = employee == null ? null
                : employee.getEmail() != null ? employee.getEmail()
                : employee.getPerfil() != null ? employee.getPerfil().getEmail() : null;
        String companyName = vacante != null && vacante.getEmpresa() != null ? vacante.getEmpresa().getNombre() : "Empresa";

        // No hay columna de estado: se deriva de la fecha y hora de la entrevista
        String status = entity.getFecha() != null && entity.getHora() != null
                && java.time.LocalDateTime.of(entity.getFecha(), entity.getHora()).isBefore(java.time.LocalDateTime.now())
                ? "COMPLETADA" : "PROGRAMADA";
        String vacancyTitle = (entity.getPostulacion() != null && entity.getPostulacion().getVacante() != null)
                ? entity.getPostulacion().getVacante().getPuesto()
                : "Vacante";
        String date = entity.getFecha() != null ? entity.getFecha().toString() : "";
        String time = entity.getHora() != null ? entity.getHora().toString() : "";
        String link = entity.getLink() != null ? entity.getLink() : "https://meet.google.com/xyz";

        return new EntrevistaResponseDTO(
                entity.getId(),
                postulacion != null ? postulacion.getPostulacionVacanteId() : null,
                candidateName,
                candidateEmail,
                vacancyTitle,
                companyName,
                date,
                time,
                link,
                status
        );
    }

    public List<EntrevistaResponseDTO> toDtoList(List<EntrevistaEntity> all) {
        if (all == null) {
            return Collections.emptyList();
        }
        return all.stream().map(this::toDto).toList();
    }
}
