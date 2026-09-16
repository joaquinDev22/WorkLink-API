package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BootcampMapper {
    
    @Mapping(target = "instructor.id", source = "instructor_id")
    BootcampEntity toEntity(BootcampRequestDTO dto);
    
    @Mapping(target = "instructor_id", source = "instructor.id")
    BootcampResponseDTO toDto(BootcampEntity entity);

    List<BootcampResponseDTO> toDtoList(List<BootcampEntity> entities);
    
    @Mapping(target = "instructor", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(BootcampRequestDTO dto, @MappingTarget BootcampEntity entity);
}
