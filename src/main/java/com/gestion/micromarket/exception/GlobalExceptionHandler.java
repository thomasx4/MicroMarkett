package com.gestion.micromarket.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gestion.micromarket.dto.MessageResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja los errores de validación de campos en el cuerpo de las solicitudes
     * (fieldName y errorMessage)
     * Se activa cuando un DTO no cumple las restricciones definidas con anotaciones
     * de validación.
     * 
     * @param ex excepción con los detalles de los campos que fallaron en la
     *           validación
     * @return mapa con el nombre de cada campo inválido y su mensaje de error con
     *         estado 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Maneja las excepciones de negocio que vienen de los service y controller
     * 
     * @param ex excepcion con el mensaje del error
     * @return mensaje de error con estado (400)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageResponseDTO> handleRuntimeException(RuntimeException ex) {
        MessageResponseDTO response = new MessageResponseDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja los errores sobre JWT, token expirado o invalido o malformado
     * 
     * @param ex excepcion con el detalle del fallo en la validación del token
     * @return mensaje de error con estado (401)
     */
    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<MessageResponseDTO> handleJwtException(io.jsonwebtoken.JwtException ex) {
        MessageResponseDTO response = new MessageResponseDTO("Token inválido: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Maneja errores de autorización (Roles no permitidos)
     * 
     * @param ex
     * @return un estado 403 FORBIDDEN
     */
    @ExceptionHandler(SecurityAuthorizationException.class)
    public ResponseEntity<MessageResponseDTO> handleSecurityException(SecurityAuthorizationException ex) {
        MessageResponseDTO response = new MessageResponseDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}
