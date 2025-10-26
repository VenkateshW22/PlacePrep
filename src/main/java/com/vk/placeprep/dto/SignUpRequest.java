package com.vk.placeprep.dto;

import lombok.Data;

/**
 * Data Transfer Object for user registration requests.
 * Contains all the necessary information required to create a new user account.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
public class SignUpRequest {
    /**
     * The full name of the user.
     * This field is required and should be between 2 and 100 characters.
     */
    private String name;
    
    /**
     * The university email address of the user.
     * This field is required and should be a valid university email format.
     * It will be used for account verification and communication.
     */
    private String universityEmail;
    
    /**
     * The user's password.
     * This field is required and should meet the application's password complexity requirements.
     * The password will be hashed before being stored in the database.
     */
    private String password;
    
    /**
     * The academic branch or department of the user (e.g., "Computer Science").
     * This field is optional but recommended for better user experience.
     */
    private String branch;
    
    /**
     * The expected or actual graduation year of the user.
     * This helps in providing relevant content and opportunities based on academic timeline.
     */
    private int graduationYear;
}
