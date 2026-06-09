package com.example.Proyecto_Final_Progra3.dto.requestdto;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorRequestDTO {

    private String nombre;
    private String apellido;
    private String titulo;
    private String presentacion;
    private Tipo tipo;
    private Integer perfil_id;
    private Integer empresa_id;
}
