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
        String candidateName = (entity.getPostulacion() != null && entity.getPostulacion().getEmployee() != null)
                ? entity.getPostulacion().getEmployee().getNombre()
                : "Candidato";
        String vacancyTitle = (entity.getPostulacion() != null && entity.getPostulacion().getVacante() != null)
                ? entity.getPostulacion().getVacante().getPuesto()
                : "Vacante";
        String date = entity.getFecha() != null ? entity.getFecha().toString() : "";
        String time = entity.getHora() != null ? entity.getHora().toString() : "";
        String link = entity.getLink() != null ? entity.getLink() : "https://meet.google.com/xyz";

        return new EntrevistaResponseDTO(
                entity.getId(),
                candidateName,
                vacancyTitle,
                date,
                time,
                link
        );
    }

    public List<EntrevistaResponseDTO> toDtoList(List<EntrevistaEntity> all) {
        if (all == null) {
            return Collections.emptyList();
        }
        return all.stream().map(this::toDto).toList();
    }
}
