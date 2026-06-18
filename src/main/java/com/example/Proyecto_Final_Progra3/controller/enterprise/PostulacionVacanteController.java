package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.dto.responsedto.PostulacionResponseDTO;
import com.example.Proyecto_Final_Progra3.service.enterprise.PostulacionVacanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vacantes/{vacanteId}/postulaciones")
@RequiredArgsConstructor
public class PostulacionVacanteController {

    private final PostulacionVacanteService postulacionService;

    @PostMapping
    public ResponseEntity<PostulacionResponseDTO> postularse(
            @PathVariable Integer vacanteId,
            @RequestParam("employeeId") Integer employeeId,
            @RequestParam("mensaje") String mensaje,
            @RequestParam(value = "cv", required = false) MultipartFile cv) {
        return ResponseEntity.ok(postulacionService.postularse(vacanteId, employeeId, mensaje, cv));
    }

    @PostMapping("/me")
    public ResponseEntity<PostulacionResponseDTO> postularseMe(
            @PathVariable Integer vacanteId,
            org.springframework.security.core.Authentication authentication,
            @RequestParam("mensaje") String mensaje,
            @RequestParam(value = "cv", required = false) MultipartFile cv) {
        return ResponseEntity.ok(postulacionService.postularseMe(authentication.getName(), vacanteId, mensaje, cv));
    }

    @GetMapping
    public ResponseEntity<List<PostulacionResponseDTO>> verPostulantes(@PathVariable Integer vacanteId) {
        return ResponseEntity.ok(postulacionService.listarPostulantes(vacanteId));
    }
}
