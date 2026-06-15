package com.example.Proyecto_Final_Progra3.controller.freelance;

import com.example.Proyecto_Final_Progra3.dto.requestdto.PostulacionProyectoRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionProyectoResponseDTO;
import com.example.Proyecto_Final_Progra3.service.freelance.PostulacionProyectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/freelance/proyectos/{proyectoId}/postulaciones")
@RequiredArgsConstructor
public class PostulacionProyectoController {

    private final PostulacionProyectoService postulacionService;

    @PostMapping
    public ResponseEntity<PostulacionProyectoResponseDTO> enviarPropuesta(
            @PathVariable Integer proyectoId,
            @Valid @RequestBody PostulacionProyectoRequestDTO dto) {
        return ResponseEntity.ok(postulacionService.postularse(proyectoId, dto));
    }

    @GetMapping
    public ResponseEntity<List<PostulacionProyectoResponseDTO>> listarPropuestas(@PathVariable Integer proyectoId) {
        return ResponseEntity.ok(postulacionService.listarPostulacionesPorProyecto(proyectoId));
    }
}
