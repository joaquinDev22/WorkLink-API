package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.requestdto.BootcampRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.BootcampResponseDTO;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
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
