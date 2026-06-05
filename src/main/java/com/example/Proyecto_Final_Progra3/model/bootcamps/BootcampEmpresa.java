package com.example.Proyecto_Final_Progra3.model.bootcamps;

import com.example.Proyecto_Final_Progra3.model.Instructor.InstructorEmpresa;
import com.example.Proyecto_Final_Progra3.model.enterprise.Empresa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "bootcamp_empresa")
public class BootcampEmpresa extends Bootcamp {

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private InstructorEmpresa instructorEmpresa;

}
