package com.vms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, String>> handleConflict(ConflictException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(UnauthorizedException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}
