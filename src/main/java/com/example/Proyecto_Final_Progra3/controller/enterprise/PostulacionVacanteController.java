package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.PostulacionVacanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/vacantes/{vacanteId}/postulaciones")
@RequiredArgsConstructor
public class PostulacionVacanteController {

    private final PostulacionVacanteService postulacionService;

    @PostMapping
    public ResponseEntity<?> postularse(
            @PathVariable Integer vacanteId,
            @RequestParam("employeeId") Integer employeeId,
            @RequestParam(value = "mensaje", required = false) String mensaje,
            @RequestParam(value = "cv", required = false) MultipartFile cv) {
        
        try {
            PostulacionResponseDTO response = postulacionService.postularse(vacanteId, employeeId, mensaje, cv);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al postularse: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PostulacionResponseDTO>> verPostulantes(@PathVariable Integer vacanteId) {
        return ResponseEntity.ok(postulacionService.listarPostulantes(vacanteId));
    }
}
