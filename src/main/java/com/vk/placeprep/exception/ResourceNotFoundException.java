package com.vk.placeprep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not found in the system.
 * Results in an HTTP 404 (Not Found) status code when thrown from a controller method.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new ResourceNotFoundException with details about the missing resource.
     *
     * @param resourceName The name/type of the resource that was not found (e.g., "User", "Experience")
     * @param fieldName   The name of the field that was used for lookup (e.g., "id", "email")
     * @param fieldValue  The value of the field that was used for lookup
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
