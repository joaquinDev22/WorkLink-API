package com.example.Proyecto_Final_Progra3.bootcamps.instructor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstructorMapper {

    @Mapping(target = "empresa.id", source = "empresa_id")
    InstructorEntity toEntity(InstructorRequestDTO dto);

    @Mapping(target = "empresa_id", source = "empresa.id")
    InstructorResponseDTO toDto(InstructorEntity entity);

    List<InstructorResponseDTO> toDtoList(List<InstructorEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    void updateEntityFromDto(InstructorRequestDTO dto, @MappingTarget InstructorEntity entity);
}
