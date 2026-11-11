package com.example.Proyecto_Final_Progra3.service.freelance.freelanceInterfaces;

import com.example.Proyecto_Final_Progra3.dto.requestdto.MedioCobroRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.MedioCobroResponseDTO;

public interface IFreelancerMedioCobroService {

    MedioCobroResponseDTO getMedioCobro(String username);

    MedioCobroResponseDTO upsertMedioCobro(String username, MedioCobroRequestDTO request);

    void deleteMedioCobro(String username);
}
