package com.example.Proyecto_Final_Progra3.repository.bootcamp;

import com.example.Proyecto_Final_Progra3.model.Employee;
import com.example.Proyecto_Final_Progra3.model.bootcamps.Bootcamp;
import com.example.Proyecto_Final_Progra3.model.bootcamps.CertificadoBootcamp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificadoBootcampRepository extends JpaRepository<CertificadoBootcamp, Integer> {
    boolean existsByBootcampAndEmployee(Bootcamp bootcamp, Employee employee);

}
