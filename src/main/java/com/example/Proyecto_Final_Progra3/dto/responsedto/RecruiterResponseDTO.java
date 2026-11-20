package com.example.Proyecto_Final_Progra3.dto.responsedto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Datos de respuesta de un recruiter asociado a una empresa")
public record RecruiterResponseDTO(
        @Schema(description = "Identificador único del recruiter", example = "1")
        Integer recruiterId,

        @Schema(description = "Nombre del recruiter", example = "María")
        String nombre,

        @Schema(description = "Apellido del recruiter", example = "González")
        String apellido,

        @Schema(description = "Correo electrónico del recruiter", example = "maria.gonzalez@empresa.com")
        String email,

        @Schema(description = "Fecha de nacimiento", example = "1992-05-15")
        LocalDate fechaNacimiento,

        @Schema(description = "Área o departamento", example = "IT & Tech Recruiting")
        String area,

        @Schema(description = "Identificador de la empresa a la que pertenece", example = "1")
        Integer empresaId,

        @Schema(description = "Nombre de la empresa asociada", example = "Tech Solutions S.A.")
        String nombreEmpresa,

        @Schema(description = "Cuándo la empresa vinculó al recruiter")
        LocalDateTime fechaVinculacion,

        @Schema(description = "Si el recruiter ya vio el aviso de que fue contratado")
        boolean vinculacionVista,

        @Schema(description = "Cuándo el recruiter pidió la baja (null si no la pidió)")
        LocalDateTime bajaSolicitadaEn,

        @Schema(description = "Desde cuándo el recruiter puede desvincularse por su cuenta si la empresa no responde")
        LocalDateTime fechaBajaHabilitada
) {}

