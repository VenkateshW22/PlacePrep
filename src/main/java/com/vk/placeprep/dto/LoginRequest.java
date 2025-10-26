package com.vk.placeprep.dto;

import lombok.Data;

/**
 * Data Transfer Object representing a user login request.
 * Contains the credentials required for user authentication.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
public class LoginRequest {
    /**
     * The university email address of the user attempting to log in.
     * This field is required and should be a valid university email format.
     */
    private String universityEmail;
    
    /**
     * The password of the user attempting to log in.
     * This field is required and should match the user's stored password.
     * The password should be sent securely over HTTPS.
     */
    private String password;
}
