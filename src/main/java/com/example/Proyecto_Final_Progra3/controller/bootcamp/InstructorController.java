package com.example.Proyecto_Final_Progra3.controller.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.requestdto.InstructorRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.InstructorResponseDTO;
import com.example.Proyecto_Final_Progra3.service.bootcamp.InstructorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/instructores")
@AllArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    public ResponseEntity<InstructorResponseDTO> crearInstructor(@Valid @RequestBody InstructorRequestDTO dto) {
        return new ResponseEntity<>(instructorService.crearInstructor(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstructorResponseDTO> modificarInstructor(@PathVariable Integer id, @Valid @RequestBody InstructorRequestDTO dto) {
        return ResponseEntity.ok(instructorService.modificarInstructor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInstructor(@PathVariable Integer id) {
        instructorService.eliminarInstructor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<InstructorResponseDTO>> listarInstructores() {
        return ResponseEntity.ok(instructorService.listarInstuctores());
    }

    @GetMapping("/bootcamps")
    public ResponseEntity<List<java.util.Map<String, Object>>> getInstructorBootcamps() {
        return ResponseEntity.ok(List.of(
            java.util.Map.of("id", 1, "title", "Bootcamp Java", "status", "Activo", "students", 20)
        ));
    }
}
