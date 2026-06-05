package com.example.Proyecto_Final_Progra3.model.bootcamps;

import com.example.Proyecto_Final_Progra3.model.Instructor.InstructorParticular;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "bootcamp_particular")
public class BootcampParticular extends Bootcamp {
    @ManyToMany
    @JoinColumn(name = "instructor_id")
    InstructorParticular instructorParticular;
}
