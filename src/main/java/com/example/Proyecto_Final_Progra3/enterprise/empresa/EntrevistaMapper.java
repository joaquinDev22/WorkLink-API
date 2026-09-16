package com.example.Proyecto_Final_Progra3.enterprise.empresa;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntrevistaMapper {
    @Mapping(target = "candidateName", expression = "java(entity.getPostulacion() != null && entity.getPostulacion().getEmployee() != null ? entity.getPostulacion().getEmployee().getNombre() : \"Candidato\")")
    @Mapping(target = "vacancyTitle", expression = "java(entity.getPostulacion() != null && entity.getPostulacion().getVacante() != null ? entity.getPostulacion().getVacante().getPuesto() : \"Vacante\")")
    @Mapping(target = "date", expression = "java(entity.getFecha() != null ? entity.getFecha().toString() : \"\")")
    @Mapping(target = "time", expression = "java(entity.getHora() != null ? entity.getHora().toString() : \"\")")
    @Mapping(target = "link", expression = "java(entity.getLink() != null ? entity.getLink() : \"https://meet.google.com/xyz\")")
    EntrevistaResponseDTO toDto(EntrevistaEntity entity);

    List<EntrevistaResponseDTO> toDtoList(List<EntrevistaEntity> all);
}

