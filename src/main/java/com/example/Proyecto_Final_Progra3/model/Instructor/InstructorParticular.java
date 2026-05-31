package com.example.Proyecto_Final_Progra3.model.Instructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "instructor_particular")
public class InstructorParticular extends Instructor {
    private String descripcion;
}
