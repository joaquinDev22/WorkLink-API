package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.responsedto.InscripcionResponseDTO;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Inscripcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InscripcionMapper {
    
    @Mapping(target = "bootcamp_id", source = "bootcamp.id")
    @Mapping(target = "employee_id", source = "employee.employeeId")
    InscripcionResponseDTO toDto(Inscripcion entity);
}
