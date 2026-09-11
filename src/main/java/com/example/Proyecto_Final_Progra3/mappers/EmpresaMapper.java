package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.EmpresaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmpresaMapper {
    EmpresaEntity toEntity(EmpresaRequestDTO dto);

    EmpresaResponseDTO toDto(EmpresaEntity entity);

    List<EmpresaResponseDTO> toDtoList(List<EmpresaEntity> entities);

    @Mapping(target = "perfil", ignore = true)
    void updateEntityFromDto(EmpresaRequestDTO dto, @MappingTarget EmpresaEntity entity);
}
