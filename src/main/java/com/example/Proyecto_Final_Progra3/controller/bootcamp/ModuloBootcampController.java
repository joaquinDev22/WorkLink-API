package com.example.Proyecto_Final_Progra3.controller.bootcamp;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/modulo")
@Tag(name = "Bootcamps", description = "Gestión de bootcamps, incluyendo creación, modificación, consulta y eliminación.")
public class ModuloBootcampController {
}
