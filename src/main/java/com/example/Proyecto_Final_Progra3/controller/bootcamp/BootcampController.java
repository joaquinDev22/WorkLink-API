package com.example.Proyecto_Final_Progra3.controller.bootcamp;

import com.example.Proyecto_Final_Progra3.dto.requestdto.BootcampRequestDTO;
import com.example.Proyecto_Final_Progra3.dto.responsedto.BootcampResponseDTO;
import com.example.Proyecto_Final_Progra3.service.bootcamp.BootcampService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bootcamps")
@AllArgsConstructor
public class BootcampController {

    private final BootcampService bootcampService;

    @PostMapping
    public ResponseEntity<BootcampResponseDTO> crearBootcamp(@Valid @RequestBody BootcampRequestDTO dto) {
        return new ResponseEntity<>(bootcampService.crearBootcamp(dto), HttpStatus.CREATED);
    }

    @PostMapping("/me")
    public ResponseEntity<BootcampResponseDTO> crearMiBootcamp(
            org.springframework.security.core.Authentication authentication, 
            @RequestBody BootcampRequestDTO dto) {
        return new ResponseEntity<>(bootcampService.crearMiBootcamp(authentication.getName(), dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BootcampResponseDTO> modificarBootcamp(@PathVariable Integer id, @Valid @RequestBody BootcampRequestDTO dto) {
        return ResponseEntity.ok(bootcampService.modificarBootcamp(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBootcamp(@PathVariable Integer id) {
        bootcampService.eliminarBootcamp(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BootcampResponseDTO>> listarBootcamps() {
        return ResponseEntity.ok(bootcampService.listarBootcamps());
    }

    @GetMapping("/me")
    public ResponseEntity<List<BootcampResponseDTO>> listarMisBootcamps(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(bootcampService.listarMisBootcamps(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BootcampResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(bootcampService.buscarPorId(id));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<BootcampResponseDTO>> listarDisponibles() {
        return ResponseEntity.ok(bootcampService.listarDisponibles());
    }

    @GetMapping("/modalidad/{modalidad}")
    public ResponseEntity<List<BootcampResponseDTO>> listarPorModalidad(@PathVariable String modalidad) {
        return ResponseEntity.ok(bootcampService.listarPorModalidad(modalidad));
    }
}
