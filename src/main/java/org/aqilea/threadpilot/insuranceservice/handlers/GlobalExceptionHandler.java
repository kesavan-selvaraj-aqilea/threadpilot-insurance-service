package org.aqilea.threadpilot.insuranceservice.handlers;

import org.aqilea.threadpilot.insuranceservice.handlers.exceptions.PersonNotFoundException;
import org.aqilea.threadpilot.insuranceservice.handlers.exceptions.VehicleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles PersonNotFoundException and returns a 404 Not Found response.
     * @param ex The PersonNotFoundException that was thrown.
     * @return ResponseEntity containing error details and HTTP status 404.
     */
    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVehicleNotFoundException(VehicleNotFoundException ex) {
        Map<String, Object> errorDetails = extractErrorDetails(ex, "/vehicles/{registrationNumber}");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles PersonNotFoundException and returns a 404 Not Found response.
     * @param ex The PersonNotFoundException that was thrown.
     * @return ResponseEntity containing error details and HTTP status 404.
     */
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePersonNotFoundException(PersonNotFoundException ex) {
        Map<String, Object> errorDetails = extractErrorDetails(ex, "/insurances/{personalIdentificationNumber}");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles all other uncaught exceptions and returns a 500 Internal Server Error response.
     * @param ex The Exception that was thrown.
     * @return ResponseEntity containing error details and HTTP status 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetails.put("error", "Internal Server Error");
        errorDetails.put("message", "An unexpected error occurred: " + ex.getMessage());
        // In a real application, avoid exposing too much internal detail about the exception
        // errorDetails.put("details", ex.toString()); // For debugging, but not for production
        // errorDetails.put("path", "unknown");

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static Map<String, Object> extractErrorDetails(Exception ex, String path) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("message", ex.getMessage());
        // errorDetails.put("path", path);
        return errorDetails;
    }
}
