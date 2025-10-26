package com.vk.placeprep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A generic response wrapper for all API responses.
 * Provides a standardized format for success/error responses across the application.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
@AllArgsConstructor
public class ApiResponse {
    /**
     * Indicates whether the API request was successful.
     */
    private boolean success;

    /**
     * A message providing details about the API response.
     * For successful operations, this might contain a success message.
     * For errors, this will contain an error message explaining what went wrong.
     */
    private String message;
}
