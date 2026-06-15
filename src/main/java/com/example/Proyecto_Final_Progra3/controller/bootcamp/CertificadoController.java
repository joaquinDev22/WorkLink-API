package com.example.Proyecto_Final_Progra3.controller.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.responsedto.CertificadoResponseDTO;
import com.example.Proyecto_Final_Progra3.service.bootcamp.CertificadoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certificados")
@AllArgsConstructor
public class CertificadoController {

    private final CertificadoService certificadoService;

    @PostMapping("/generar/{inscripcionId}")
    public ResponseEntity<CertificadoResponseDTO> generarCertificado(@PathVariable Integer inscripcionId) {
        return new ResponseEntity<>(certificadoService.generarCertificado(inscripcionId), HttpStatus.CREATED);
    }
}
