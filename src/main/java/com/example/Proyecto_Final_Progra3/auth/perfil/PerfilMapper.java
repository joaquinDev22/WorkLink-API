package com.example.Proyecto_Final_Progra3.auth.perfil;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PerfilMapper {

    @Mapping(target = "rol", ignore = true)
    PerfilEntity toEntity(PerfilRequestDTO dto);

    @Mapping(target = "rol", source = "rol.nombre")
    PerfilResponseDTO toDto(PerfilEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "rol", ignore = true)
    void updateEntityFromDto(PerfilRequestDTO dto, @MappingTarget PerfilEntity entity);
}

