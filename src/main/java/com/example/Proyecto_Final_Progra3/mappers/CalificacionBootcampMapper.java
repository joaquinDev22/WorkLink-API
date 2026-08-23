package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.responsedto.CalificacionBootcampResponseDTO;
import com.example.Proyecto_Final_Progra3.model.bootcamps.CalificacionBootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CalificacionBootcampMapper {

    @Mapping(target = "bootcamp_id", source = "bootcamp.id")
    @Mapping(target = "employee_id", source = "employee.employeeId")
    CalificacionBootcampResponseDTO toDto(CalificacionBootcampEntity entity);
}

