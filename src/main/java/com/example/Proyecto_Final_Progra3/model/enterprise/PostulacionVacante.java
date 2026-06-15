package com.example.Proyecto_Final_Progra3.model.enterprise;
import com.example.Proyecto_Final_Progra3.model.employee.Employee;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "postulacion_vacante")
public class PostulacionVacante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postulacion_id")
    private Integer postulacionVacanteId;

    @Column(name = "fecha_postulacion", nullable = false)
    private LocalDate fechaPostulacion;

    @Column(name = "estado_postulacion", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPostulacion estado;

    @Lob
    @Column(name = "cv", columnDefinition = "LONGBLOB")
    private byte[] cv;

    @Column(length = 250)
    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "vacante_id")
    private Vacante vacante;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
