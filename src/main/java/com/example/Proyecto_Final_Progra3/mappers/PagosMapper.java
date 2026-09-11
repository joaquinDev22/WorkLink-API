package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.responsedto.PagosResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.pagos.PagosEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PagosMapper {

    @Mapping(target = "pagoId", source = "pagosId")
    @Mapping(target = "proyectoId", source = "proyecto.id")
    PagosResponseDTO toDto(PagosEntity entity);

    List<PagosResponseDTO> toDtoList(List<PagosEntity> entities);
}
