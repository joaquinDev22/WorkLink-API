package com.example.Proyecto_Final_Progra3.controller.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.requestdto.CalificacionBootcampRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.CalificacionBootcampResponseDTO;
import com.example.Proyecto_Final_Progra3.service.bootcamp.CalificacionBootcampService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/calificaciones")
@AllArgsConstructor
@Tag(name = "Bootcamps", description = "Gestión de bootcamps, incluyendo creación, modificación, consulta y eliminación.")
public class CalificacionBootcampController {

    private final CalificacionBootcampService calificacionService;

    @Operation(summary = "Calificar bootcamp", description = "Permite registrar una calificación y comentario para un bootcamp específico.")
    @PostMapping("/bootcamps/{bootcampId}")
    public ResponseEntity<CalificacionBootcampResponseDTO> calificarBootcamp(
            @PathVariable Integer bootcampId, 
            @Valid @RequestBody CalificacionBootcampRequestDTO dto) {
        return new ResponseEntity<>(calificacionService.calificarBootcamp(bootcampId, dto), HttpStatus.CREATED);
    }
}
