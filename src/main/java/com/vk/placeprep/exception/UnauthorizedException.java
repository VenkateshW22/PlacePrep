package com.vk.placeprep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user attempts to access a resource without proper authentication
 * or when authentication fails. Results in an HTTP 401 (Unauthorized) status code
 * when thrown from a controller method.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    
    /**
     * Constructs a new UnauthorizedException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnauthorizedException with the specified detail message and cause.
     *
     * @param message The detail message explaining the reason for the exception
     * @param cause The cause (which is saved for later retrieval by the getCause() method)
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
