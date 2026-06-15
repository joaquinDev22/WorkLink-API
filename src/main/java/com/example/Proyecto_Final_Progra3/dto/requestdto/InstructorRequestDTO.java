package com.example.Proyecto_Final_Progra3.dto.requestdto;
import com.example.Proyecto_Final_Progra3.model.bootcamps.enums.Tipo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorRequestDTO {
    @NotBlank(message = "El nombre del instructor no puede estar vacio")
    private String nombre;
    @NotBlank(message = "El apellido del instructor no puede estar vacio")
    private String apellido;
    private String titulo;
    private String presentacion;
    private Tipo tipo;
    private Integer perfil_id;
    private Integer empresa_id;
}
