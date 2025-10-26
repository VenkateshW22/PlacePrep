package com.vk.placeprep.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating user profile information.
 * Contains fields that can be updated by users in their profile.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    /**
     * The updated full name of the user.
     * Must be between 2 and 100 characters long.
     */
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    /**
     * The updated email address of the user.
     * Must be a valid email format.
     */
    @Email(message = "Email should be valid")
    private String email;
    
    /**
     * The user's current password for verification.
     * Required when changing the password.
     * Must meet the password complexity requirements if provided.
     */
    @Pattern(regexp = "^$|^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
    private String currentPassword;
    
    /**
     * The new password to set for the user's account.
     * Must meet the password complexity requirements if provided.
     * Requires currentPassword to be provided.
     */
    @Pattern(regexp = "^$|^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
    private String newPassword;
    
    /**
     * The updated academic branch or department of the user.
     * This field is optional.
     */
    private String branch;
    /**
     * The updated graduation year of the user.
     * Used for providing relevant content based on academic timeline.
     */
    private Integer graduationYear;
    
    /**
     * The URL to the user's LinkedIn profile.
     * Should be a valid URL if provided.
     */
    private String linkedinUrl;
    
    /**
     * The URL to the user's GitHub profile.
     * Should be a valid URL if provided.
     */
    private String githubUrl;
}
