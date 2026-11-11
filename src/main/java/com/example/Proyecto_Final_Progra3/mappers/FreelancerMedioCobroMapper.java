package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.requestdto.MedioCobroRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.MedioCobroResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.FreelancerMedioCobroEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FreelancerMedioCobroMapper {

    MedioCobroResponseDTO toDto(FreelancerMedioCobroEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "freelancerId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FreelancerMedioCobroEntity toEntity(MedioCobroRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "freelancerId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(MedioCobroRequestDTO dto, @MappingTarget FreelancerMedioCobroEntity entity);
}
