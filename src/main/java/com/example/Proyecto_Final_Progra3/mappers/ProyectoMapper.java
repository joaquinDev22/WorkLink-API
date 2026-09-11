package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.responsedto.ProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.ProyectoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProyectoMapper {

    @Mapping(target = "proyectoId", source = "id")
    @Mapping(target = "clienteId", source = "client.clienteId")
    @Mapping(target = "nombreCliente", source = "entity", qualifiedByName = "mapNombreCliente")
    @Mapping(target = "estado", source = "entity", qualifiedByName = "mapEstado")
    @Mapping(target = "nombreFreelancer", source = "entity", qualifiedByName = "mapNombreFreelancer")
    @Mapping(target = "habilidades", source = "habilidades", qualifiedByName = "mapHabilidades")
    ProyectoResponseDTO toDto(ProyectoEntity entity);

    List<ProyectoResponseDTO> toDtoList(List<ProyectoEntity> entities);

    @Named("mapNombreCliente")
    default String mapNombreCliente(ProyectoEntity entity) {
        if (entity == null || entity.getClient() == null) {
            return "Cliente Desconocido";
        }
        return entity.getClient().getFirstName() + " " + entity.getClient().getLastName();
    }

    @Named("mapEstado")
    default String mapEstado(ProyectoEntity entity) {
        if (entity == null || entity.getEstado() == null) {
            return "BUSCANDO_TALENTO";
        }
        return entity.getEstado().name();
    }

    @Named("mapNombreFreelancer")
    default String mapNombreFreelancer(ProyectoEntity entity) {
        if (entity == null || entity.getFreelancer() == null) {
            return null;
        }
        return entity.getFreelancer().getNombre() + " " + entity.getFreelancer().getApellido();
    }

    @Named("mapHabilidades")
    default List<String> mapHabilidades(List<String> habilidades) {
        if (habilidades == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(habilidades);
    }
}
