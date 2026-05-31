package com.example.Proyecto_Final_Progra3.model.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="recruiter")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Recruiter{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recruiter_id")
    private int recruiterId;


}
