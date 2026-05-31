package com.example.Proyecto_Final_Progra3.model.freelancer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="cliente_particular")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteParticular {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cliente_particular_id")
    private int cliente_id;
}
