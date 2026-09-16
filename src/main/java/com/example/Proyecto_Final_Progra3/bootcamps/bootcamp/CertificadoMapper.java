package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CertificadoMapper {

    @Mapping(target = "bootcamp_id", source = "bootcamp.id")
    @Mapping(target = "employee_id", source = "employee.employeeId")
    CertificadoResponseDTO toDto(CertificadoBootcampEntity entity);
}

