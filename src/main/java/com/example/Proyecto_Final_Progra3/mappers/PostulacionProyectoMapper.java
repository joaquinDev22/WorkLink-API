package com.example.Proyecto_Final_Progra3.mappers;

import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.model.freelance.proyecto.PostulacionProyectoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostulacionProyectoMapper {

    @Mapping(target = "postulacionId", source = "id")
    @Mapping(target = "costoPropuesto", source = "costo")
    @Mapping(target = "empleadoId", source = "employee.employeeId")
    @Mapping(target = "nombreEmpleado", source = "entity", qualifiedByName = "mapNombreEmpleado")
    @Mapping(target = "proyectoId", source = "proyecto.id")
    PostulacionProyectoResponseDTO toDto(PostulacionProyectoEntity entity);

    List<PostulacionProyectoResponseDTO> toDtoList(List<PostulacionProyectoEntity> entities);

    @Named("mapNombreEmpleado")
    default String mapNombreEmpleado(PostulacionProyectoEntity entity) {
        if (entity == null || entity.getEmployee() == null) {
            return "Empleado Desconocido";
        }
        return entity.getEmployee().getNombre() + " " + entity.getEmployee().getApellido();
    }
}
