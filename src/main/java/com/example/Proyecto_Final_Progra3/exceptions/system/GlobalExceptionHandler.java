package com.example.Proyecto_Final_Progra3.exceptions.system;

import com.example.Proyecto_Final_Progra3.dto.responsedto.ErrorResponseDTO;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BadRequestException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.ResourceNotFoundException;
import com.example.Proyecto_Final_Progra3.exceptions.domain.UnauthorizedAccessException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // HTTP 400 - Validación de campos DTO (@Valid en Controller)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ErrorResponseDTO error = new ErrorResponseDTO("Error de validación", errors, LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // HTTP 400 - Validación de parámetros o entidades (ConstraintViolationException)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .toList();
        ErrorResponseDTO error = new ErrorResponseDTO("Error de validación de campos", errors, LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // HTTP 400 - Solicitud Mal Formada o Parámetros Inválidos de Negocio
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Solicitud inválida", List.of(ex.getMessage()), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // HTTP 400 - Parámetro @RequestParam obligatorio ausente
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingParameter(MissingServletRequestParameterException ex) {
        String msg = "El parámetro obligatorio '" + ex.getParameterName() + "' (tipo " + ex.getParameterType() + ") no fue enviado en la petición";
        ErrorResponseDTO error = new ErrorResponseDTO("Parámetro requerido ausente", List.of(msg), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // HTTP 400 - Parte multipart obligatoria ausente (ej. archivo en upload)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingRequestPart(MissingServletRequestPartException ex) {
        String msg = "La parte multipart requerida '" + ex.getRequestPartName() + "' no se encuentra en la petición";
        ErrorResponseDTO error = new ErrorResponseDTO("Archivo o parte ausente", List.of(msg), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // HTTP 400 - Cuerpo JSON Inválido o Malformado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String causa = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "Formato JSON malformado";
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Cuerpo de solicitud inválido",
                List.of("Error al parsear el JSON de la petición: " + causa),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // HTTP 400 - Tipo de Parámetro Incorrecto en URL o Query (ej. string en vez de int o UUID)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String tipoRequerido = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "válido";
        String msg = "El parámetro '" + ex.getName() + "' con valor '" + ex.getValue() + "' no tiene el formato esperado (" + tipoRequerido + ")";
        ErrorResponseDTO error = new ErrorResponseDTO("Parámetro con formato inválido", List.of(msg), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // HTTP 400 - Argumentos ilegales o estados inválidos en lógica
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponseDTO> handleIllegalArguments(RuntimeException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Petición inválida",
                List.of(ex.getMessage() != null ? ex.getMessage() : "Argumento o estado inválido"),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // HTTP 403 - Autenticado, pero sin permiso sobre este recurso (no es dueño, rol incorrecto)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        // 403 y no 401: el usuario está autenticado pero no tiene permiso; un 401 haría que el front cierre la sesión
        ErrorResponseDTO error = new ErrorResponseDTO("Acceso denegado", List.of(ex.getMessage()), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // HTTP 401 - Credenciales Inválidas o Fallo de Autenticación
    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(Exception ex) {
        String detalle = ex instanceof BadCredentialsException && "Bad credentials".equals(ex.getMessage())
                ? "Usuario o contraseña incorrectos"
                : (ex.getMessage() != null ? ex.getMessage() : "No se pudo autenticar la solicitud");
        ErrorResponseDTO error = new ErrorResponseDTO("Autenticación fallida", List.of(detalle), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // HTTP 403 - Acceso Denegado por Rol / Permisos Insuficientes
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleSpringAccessDenied(AccessDeniedException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Acceso denegado",
                List.of("No posees los permisos requeridos para ejecutar esta acción en este recurso"),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // HTTP 404 - Recurso No Encontrado por ID o criterio
    @ExceptionHandler({ResourceNotFoundException.class, NoSuchElementException.class, EntityNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(Exception ex) {
        String detalle = ex.getMessage() != null ? ex.getMessage() : "El recurso solicitado no existe en la base de datos";
        ErrorResponseDTO error = new ErrorResponseDTO("Recurso no encontrado", List.of(detalle), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // HTTP 404 - Ruta / Endpoint no encontrado en Spring MVC
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFound(NoResourceFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Ruta no encontrada",
                List.of("El endpoint '" + ex.getResourcePath() + "' no existe en el servidor"),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // HTTP 405 - Método HTTP No Soportado
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Método no permitido",
                List.of("El método HTTP " + ex.getMethod() + " no está soportado en este endpoint"),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // HTTP 413 - Tamaño de subida excedido
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Archivo demasiado grande",
                List.of("El tamaño del archivo supera el límite permitido por el servidor"),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONTENT_TOO_LARGE);
    }

    // HTTP 415 - Tipo de contenido no soportado (Content-Type)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Formato de contenido no soportado",
                List.of("El Content-Type '" + ex.getContentType() + "' no es soportado. Se esperaba formato compatible."),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    // HTTP 406 - Encabezado Accept no compatible
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Formato de respuesta no aceptable",
                List.of("El servidor no puede responder en el formato solicitado por el cliente."),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    // HTTP 409 - Conflicto de Base de Datos (Restricción UNIQUE / FK)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Error de integridad de datos SQL: {}", ex.getMessage(), ex);
        String causa = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "Restricción de unicidad o clave foránea violada";
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Conflicto de datos",
                List.of("No se pudo completar la operación: " + causa),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // HTTP 422 - Violación de Regla de Negocio
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessRuleException(BusinessRuleException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO("Regla de negocio violada", List.of(ex.getMessage()), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    // HTTP 400/422 - Error de validación al hacer flush en transacción JPA
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErrorResponseDTO> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable root = ex.getRootCause();
        if (root instanceof ConstraintViolationException cve) {
            List<String> errors = cve.getConstraintViolations().stream()
                    .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                    .toList();
            ErrorResponseDTO error = new ErrorResponseDTO("Error de validación en base de datos", errors, LocalDate.now());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        String causa = root != null ? root.getMessage() : ex.getMessage();
        ErrorResponseDTO error = new ErrorResponseDTO("Error en transacción", List.of(causa), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // HTTP 500 - Referencia nula no controlada (con ubicación precisa en vez de mensaje vacío)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponseDTO> handleNullPointer(NullPointerException ex) {
        log.error("NullPointerException inesperado: {}", ex.getMessage(), ex);
        String ubicacion = (ex.getStackTrace() != null && ex.getStackTrace().length > 0)
                ? ex.getStackTrace()[0].getFileName() + ":" + ex.getStackTrace()[0].getLineNumber()
                : "desconocido";
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Error interno: Referencia nula",
                List.of("Ocurrió un error inesperado en " + ubicacion + ". " + (ex.getMessage() != null ? ex.getMessage() : "")),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // HTTP 500 - Error de Acceso a Base de Datos
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataAccessException(DataAccessException ex) {
        log.error("Error de acceso a base de datos: {}", ex.getMessage(), ex);
        String causa = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        ErrorResponseDTO error = new ErrorResponseDTO("Error en base de datos", List.of(causa != null ? causa : "Error SQL"), LocalDate.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // HTTP 500 - Salvaguarda para Excepciones Inesperadas No Capturadas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        log.error("Error interno no controlado [{}]: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        String detalle = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getClass().getSimpleName() + ": " + ex.getMessage()
                : "Excepción inesperada de tipo " + ex.getClass().getSimpleName();
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Error interno: " + ex.getClass().getSimpleName(),
                List.of(detalle),
                LocalDate.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
