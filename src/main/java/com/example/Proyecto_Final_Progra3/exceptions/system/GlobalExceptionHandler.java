package com.example.Proyecto_Final_Progra3.exceptions.system;

import com.example.Proyecto_Final_Progra3.dto.responsedto.ErrorResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BadRequestException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
     // HTTP 400 - Error of Validation of fields (@Valid en Controller)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ErrorResponseDTO error = new ErrorResponseDTO("Error de validación", errors, LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    // HTTP 400 - Solicitud Mal Formada o Parámetros Inválidos
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Solicitud inválida", List.of(ex.getMessage()), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    // HTTP 400 - Cuerpo de Solicitud Inválido
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadable(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Cuerpo de solicitud inválido", List.of("El cuerpo JSON enviado en la petición no es válido o está malformado"), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    // HTTP 404 - Recurso No Encontrado (ID no existe en BD)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Recurso no encontrado", List.of(ex.getMessage()), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    // HTTP 422 - Violación de Regla de Negocio
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessRuleException(BusinessRuleException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Regla de negocio violada", List.of(ex.getMessage()), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    // HTTP 403 - Acceso No Autorizado sobre Recurso Ajeno
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Acceso denegado", List.of(ex.getMessage()), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
    // HTTP 403 - Acceso Denegado
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleSpringAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Acceso denegado", List.of("No posees los permisos requeridos para ejecutar esta acción"), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // HTTP 409 - Conflicto de Base de Datos (Restricción UNIQUE / FK)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Error de datos SQL: {}", ex.getMessage(), ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Conflicto de datos", 
                List.of("No se pudo completar la operación debido a un conflicto de datos (ej. usuario o correo ya registrado)"), 
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    // HTTP 401 - Credenciales Inválidas de Autenticación
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Autenticación fallida", List.of("Usuario o contraseña incorrectos"), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    // HTTP 500 - Salvaguarda para Excepciones Inesperadas No Capturadas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        log.error("Error interno no controlado: {}", ex.getMessage(), ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Error interno del servidor",
                List.of("Ha ocurrido un error inesperado. Por favor intenta más tarde."),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
