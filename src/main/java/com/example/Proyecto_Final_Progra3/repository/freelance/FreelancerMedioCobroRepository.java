package com.example.Proyecto_Final_Progra3.repository.freelance;

import com.example.Proyecto_Final_Progra3.model.freelance.FreelancerMedioCobroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreelancerMedioCobroRepository extends JpaRepository<FreelancerMedioCobroEntity, Long> {

    Optional<FreelancerMedioCobroEntity> findByFreelancerId(Long freelancerId);

    void deleteByFreelancerId(Long freelancerId);
}
