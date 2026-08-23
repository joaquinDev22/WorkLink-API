package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.responsedto.CertificadoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.bootcamps.CertificadoBootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CertificadoMapper {

    @Mapping(target = "bootcamp_id", source = "bootcamp.id")
    @Mapping(target = "employee_id", source = "employee.employeeId")
    CertificadoResponseDTO toDto(CertificadoBootcampEntity entity);
}

