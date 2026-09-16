package com.example.Proyecto_Final_Progra3.bootcamps.instructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/instructores")
@AllArgsConstructor
@Tag(name = "Bootcamps", description = "Gestión de bootcamps, incluyendo creación, modificación, consulta y eliminación.")
public class InstructorController {

    private final IInstructorService instructorService;

    @Operation(summary = "Crear instructor", description = "Permite registrar un nuevo instructor en la plataforma.")
    @PostMapping
    public ResponseEntity<InstructorResponseDTO> crearInstructor(@Valid @RequestBody InstructorRequestDTO dto) {
        return new ResponseEntity<>(instructorService.crearInstructor(dto), HttpStatus.CREATED);
    }

    @Parameter(name = "id", description = "Identificador único del instructor", example = "1")
    @Operation(summary = "Modificar instructor", description = "Actualiza la información de un instructor existente mediante su identificador.")
    @PutMapping("/{id}")
    public ResponseEntity<InstructorResponseDTO> modificarInstructor(@PathVariable Integer id, @Valid @RequestBody InstructorRequestDTO dto) {
        return ResponseEntity.ok(instructorService.modificarInstructor(id, dto));
    }

    @Parameter(name = "id", description = "Identificador único del instructor", example = "1")
    @Operation(summary = "Eliminar instructor", description = "Elimina un instructor registrado en el sistema utilizando su identificador.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInstructor(@PathVariable Integer id) {
        instructorService.eliminarInstructor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar instructores", description = "Obtiene la lista completa de instructores registrados en la plataforma.")
    @GetMapping
    public ResponseEntity<List<InstructorResponseDTO>> listarInstructores() {
        return ResponseEntity.ok(instructorService.listarInstuctores());
    }

    @Operation(summary = "Consultar bootcamps del instructor", description = "Obtiene los bootcamps asociados al instructor junto con información relevante como estado y cantidad de estudiantes.")
    @GetMapping("/bootcamps")
    public ResponseEntity<List<java.util.Map<String, Object>>> getInstructorBootcamps() {
        return ResponseEntity.ok(List.of(
            java.util.Map.of("id", 1, "title", "Bootcamp Java", "status", "Activo", "students", 20)
        ));
    }
}
