package com.example.Proyecto_Final_Progra3.model.freelance;

import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name="postulacion_proyecto")
public class PostulacionProyecto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_proy_id")
    private Integer id;

    private String descripcion;

    @Column(name="valor_propuesto")
    private Double costo;

    @Column(name="fecha_postulacion")
    private LocalDate fechaPostulacion;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name="proyecto_id")
    private Proyecto proyecto;
}
