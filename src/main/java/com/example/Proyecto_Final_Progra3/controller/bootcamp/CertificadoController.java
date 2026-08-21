package com.example.Proyecto_Final_Progra3.controller.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.responsedto.CertificadoResponseDTO;
import com.example.Proyecto_Final_Progra3.service.bootcamp.bootcampInterfaces.ICertificadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certificados")
@AllArgsConstructor
@Tag(name = "Bootcamps", description = "Gestión de bootcamps, incluyendo creación, modificación, consulta y eliminación.")
public class CertificadoController {

    private final ICertificadoService certificadoService;

    @Parameter(name = "inscripcionId", description = "Identificador de la inscripción para la cual se generará el certificado", example = "1")
    @Operation(summary = "Generar certificado", description = "Genera un certificado para una inscripción aprobada y devuelve la información del certificado emitido.")
    @PostMapping("/generar/{inscripcionId}")
    public ResponseEntity<CertificadoResponseDTO> generarCertificado(@PathVariable Integer inscripcionId) {
        return new ResponseEntity<>(certificadoService.generarCertificado(inscripcionId), HttpStatus.CREATED);
    }
}
