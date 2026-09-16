package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostulacionVacanteMapper {

    @Mapping(target = "postulacionId", source = "postulacionVacanteId")
    @Mapping(target = "vacanteId", source = "vacante.vacanteId")
    @Mapping(target = "puestoVacante", source = "vacante.puesto")
    @Mapping(target = "employeeId", source = "employee.employeeId")
    @Mapping(target = "nombreCandidato", source = "entity", qualifiedByName = "mapNombreCandidato")
    @Mapping(target = "emailCandidato", source = "employee.email")
    @Mapping(target = "tieneCvSubido", source = "entity", qualifiedByName = "mapTieneCv")
    PostulacionResponseDTO toDto(PostulacionVacanteEntity entity);

    List<PostulacionResponseDTO> toDtoList(List<PostulacionVacanteEntity> entities);

    @Named("mapNombreCandidato")
    default String mapNombreCandidato(PostulacionVacanteEntity entity) {
        if (entity == null || entity.getEmployee() == null) {
            return "Candidato Desconocido";
        }
        return entity.getEmployee().getNombre() + " " + entity.getEmployee().getApellido();
    }

    @Named("mapTieneCv")
    default boolean mapTieneCv(PostulacionVacanteEntity entity) {
        return entity != null && entity.getCv() != null && entity.getCv().length > 0;
    }
}
