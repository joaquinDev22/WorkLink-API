package com.example.Proyecto_Final_Progra3.enterprise.vacante;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VacanteMapper {

    @Mapping(target = "vacanteId", source = "vacanteId")
    @Mapping(target = "empresaId", source = "empresa.id")
    @Mapping(target = "nombreEmpresa", source = "entity", qualifiedByName = "mapNombreEmpresa")
    @Mapping(target = "etiquetas", source = "etiquetas", qualifiedByName = "mapEtiquetas")
    VacanteResponseDTO toDto(VacanteEntity entity);

    List<VacanteResponseDTO> toDtoList(List<VacanteEntity> entities);

    @Named("mapNombreEmpresa")
    default String mapNombreEmpresa(VacanteEntity entity) {
        if (entity == null || entity.getEmpresa() == null) {
            return "Empresa Desconocida";
        }
        return entity.getEmpresa().getNombre();
    }

    @Named("mapEtiquetas")
    default List<String> mapEtiquetas(List<String> etiquetas) {
        if (etiquetas == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(etiquetas);
    }
}
