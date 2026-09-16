package com.example.Proyecto_Final_Progra3.bootcamps.bootcamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Entity
@Immutable
@Table(name = "vw_resumen_bootcamps")
@Getter
@NoArgsConstructor
public class ResumenBootcampEntity {
    @Id
    private Integer bootcampId;
    private String bootcampTitulo;
    private String instructorNombre;
    private String modalidad;
    private Integer cuposDisponibles;
    private Long totalInscriptos;
    private BigDecimal rating;
}
