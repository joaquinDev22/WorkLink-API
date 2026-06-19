package com.example.Proyecto_Final_Progra3.controller.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.requestdto.BootcampRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.BootcampResponseDTO;
import com.example.Proyecto_Final_Progra3.service.bootcamp.BootcampService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bootcamps")
@AllArgsConstructor
@Tag(name = "Bootcamps", description = "Gestión de bootcamps, incluyendo creación, modificación, consulta y eliminación.")
public class BootcampController {

    private final BootcampService bootcampService;

    @Operation(summary = "Crear bootcamp", description = "Permite registrar un nuevo bootcamp en la plataforma.")
    @PostMapping
    public ResponseEntity<BootcampResponseDTO> crearBootcamp(@Valid @RequestBody BootcampRequestDTO dto) {
        return new ResponseEntity<>(bootcampService.crearBootcamp(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Crear mi bootcamp", description = "Permite al usuario autenticado crear un bootcamp asociado a su cuenta.")
    @PostMapping("/me")
    public ResponseEntity<BootcampResponseDTO> crearMiBootcamp(
            org.springframework.security.core.Authentication authentication,
            @RequestBody BootcampRequestDTO dto) {
        return new ResponseEntity<>(bootcampService.crearMiBootcamp(authentication.getName(), dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Modificar bootcamp", description = "Actualiza la información de un bootcamp existente mediante su identificador.")
    @PutMapping("/{id}")
    public ResponseEntity<BootcampResponseDTO> modificarBootcamp(@PathVariable Integer id, @Valid @RequestBody BootcampRequestDTO dto) {
        return ResponseEntity.ok(bootcampService.modificarBootcamp(id, dto));
    }

    @Operation(summary = "Eliminar bootcamp", description = "Elimina un bootcamp registrado en el sistema utilizando su identificador.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBootcamp(@PathVariable Integer id) {
        bootcampService.eliminarBootcamp(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Listar bootcamps", description = "Obtiene la lista completa de bootcamps registrados en la plataforma.")
    @GetMapping
    public ResponseEntity<List<BootcampResponseDTO>> listarBootcamps() {
        return ResponseEntity.ok(bootcampService.listarBootcamps());
    }

    @Operation(summary = "Listar mis bootcamps", description = "Obtiene todos los bootcamps asociados al usuario autenticado.")
    @GetMapping("/me")
    public ResponseEntity<List<BootcampResponseDTO>> listarMisBootcamps(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(bootcampService.listarMisBootcamps(authentication.getName()));
    }

    @Operation(summary = "Buscar bootcamp por ID", description = "Obtiene la información detallada de un bootcamp a partir de su identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<BootcampResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(bootcampService.buscarPorId(id));
    }

    @Operation(summary = "Listar bootcamps disponibles", description = "Obtiene todos los bootcamps que se encuentran disponibles para inscripción.")
    @GetMapping("/disponibles")
    public ResponseEntity<List<BootcampResponseDTO>> listarDisponibles() {
        return ResponseEntity.ok(bootcampService.listarDisponibles());
    }

    @Operation(summary = "Buscar bootcamps por modalidad", description = "Obtiene los bootcamps filtrados según la modalidad especificada.")
    @GetMapping("/modalidad/{modalidad}")
    public ResponseEntity<List<BootcampResponseDTO>> listarPorModalidad(@PathVariable String modalidad) {
        return ResponseEntity.ok(bootcampService.listarPorModalidad(modalidad));
    }
}
