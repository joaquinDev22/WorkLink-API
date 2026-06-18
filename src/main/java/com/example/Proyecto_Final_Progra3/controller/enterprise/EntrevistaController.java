package com.example.Proyecto_Final_Progra3.controller.enterprise;

import com.example.Proyecto_Final_Progra3.model.enterprise.Entrevista;
import com.example.Proyecto_Final_Progra3.repository.enterprise.EntrevistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recruiter/entrevistas")
@RequiredArgsConstructor
public class EntrevistaController {

    private final EntrevistaRepository entrevistaRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getEntrevistas() {
        // En una app real, aquí obtendrías el ID de la empresa del usuario logueado.
        // Como simplificación devolvemos todas o puedes filtrar.
        List<Entrevista> entrevistas = entrevistaRepository.findAll();

        List<Map<String, Object>> response = entrevistas.stream().map(e -> {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", e.getId());
            map.put("name", e.getPostulacion() != null && e.getPostulacion().getEmployee() != null 
                    ? e.getPostulacion().getEmployee().getNombre() : "Candidato");
            map.put("vacancy", e.getPostulacion() != null && e.getPostulacion().getVacante() != null
                    ? e.getPostulacion().getVacante().getPuesto() : "Vacante");
            map.put("date", e.getFecha() != null ? e.getFecha().toString() : "");
            map.put("time", e.getHora() != null ? e.getHora().toString() : "");
            map.put("link", e.getLink() != null ? e.getLink() : "https://meet.google.com/xyz");
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
