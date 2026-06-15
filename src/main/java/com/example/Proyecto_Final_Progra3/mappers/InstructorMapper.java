package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.requestdto.InstructorRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.InstructorResponseDTO;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Instructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    @Mapping(target = "empresa.id", source = "empresa_id")
    Instructor toEntity(InstructorRequestDTO dto);

    @Mapping(target = "empresa_id", source = "empresa.id")
    InstructorResponseDTO toDto(Instructor entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    void updateEntityFromDto(InstructorRequestDTO dto, @MappingTarget Instructor entity);
}
