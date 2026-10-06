package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.requestdto.RecruiterRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.RecruiterResponseDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.RecruiterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecruiterMapper {

    @Mapping(target = "recruiterId", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "perfil", ignore = true)
    RecruiterEntity toEntity(RecruiterRequestDTO dto);

    @Mapping(target = "recruiterId", source = "recruiterId")
    @Mapping(target = "empresaId", source = "entity", qualifiedByName = "mapEmpresaId")
    @Mapping(target = "nombreEmpresa", source = "entity", qualifiedByName = "mapNombreEmpresa")
    RecruiterResponseDTO toDto(RecruiterEntity entity);

    List<RecruiterResponseDTO> toDtoList(List<RecruiterEntity> entities);

    @Mapping(target = "recruiterId", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "perfil", ignore = true)
    void updateEntityFromDto(RecruiterRequestDTO dto, @MappingTarget RecruiterEntity entity);

    @Named("mapEmpresaId")
    default Integer mapEmpresaId(RecruiterEntity entity) {
        if (entity == null || entity.getEmpresa() == null) {
            return null;
        }
        return entity.getEmpresa().getId();
    }

    @Named("mapNombreEmpresa")
    default String mapNombreEmpresa(RecruiterEntity entity) {
        if (entity == null || entity.getEmpresa() == null) {
            return "Empresa Desconocida";
        }
        return entity.getEmpresa().getNombre();
    }
}
