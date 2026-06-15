package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.responsedto.EmpresaResponseDTO;
import com.example.Proyecto_Final_Progra3.dto.requestdto.EmpresaRequestDTO;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {
    Empresa toEntity(EmpresaRequestDTO dto);

    EmpresaResponseDTO toDto(Empresa entity);

    @Mapping(target = "perfil", ignore = true)
    void updateEntityFromDto(EmpresaRequestDTO dto, @MappingTarget Empresa entity);
}
