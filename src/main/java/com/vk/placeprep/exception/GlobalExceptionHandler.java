package com.vk.placeprep.exception;

import com.vk.placeprep.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the application.
 * This class handles exceptions thrown by controllers and returns appropriate HTTP responses.
 * It provides centralized exception handling across all @RequestMapping methods.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException by returning a 404 Not Found response.
     * This exception is thrown when a requested resource is not found in the system.
     *
     * @param ex      The caught ResourceNotFoundException
     * @param request The web request that resulted in the exception
     * @return ResponseEntity containing an error response with HTTP 404 status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, 
            WebRequest request) {
        
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles all other uncaught exceptions by returning a 500 Internal Server Error response.
     * This is a catch-all handler for any exception not specifically handled by other methods.
     *
     * @param ex      The caught Exception
     * @param request The web request that resulted in the exception
     * @return ResponseEntity containing an error response with HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(
            Exception ex, 
            WebRequest request) {
        
        String errorMessage = "An unexpected error occurred: " + ex.getLocalizedMessage();
        ApiResponse apiResponse = new ApiResponse(false, errorMessage);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
