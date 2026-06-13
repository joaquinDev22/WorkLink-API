package com.example.Proyecto_Final_Progra3.model.enterprise;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name="recruiter")
public class Recruiter{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recruiter_id")
    private Integer recruiterId;


}
