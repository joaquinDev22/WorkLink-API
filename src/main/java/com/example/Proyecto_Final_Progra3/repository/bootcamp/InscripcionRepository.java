package com.example.Proyecto_Final_Progra3.repository.bootcamp;

import com.example.Proyecto_Final_Progra3.model.employee.EmployeeEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.BootcampEntity;
import com.example.Proyecto_Final_Progra3.model.bootcamps.InscripcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<InscripcionEntity, Integer> {
    boolean existsByBootcampAndEmployee(BootcampEntity bootcamp, EmployeeEntity employee);

    Optional<InscripcionEntity> findByBootcampAndEmployee(BootcampEntity bootcamp, EmployeeEntity employee);
    
    List<InscripcionEntity> findByEmployee(EmployeeEntity employee);

    @Procedure(procedureName = "sp_inscribir_bootcamp")
    String inscribirBootcamp(
            @Param("p_bootcamp_id") Integer bootcampId,
            @Param("p_employee_id") Integer employeeId
    );
}
