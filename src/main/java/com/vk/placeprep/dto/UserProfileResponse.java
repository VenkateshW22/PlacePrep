package com.vk.placeprep.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a user's profile information in responses.
 * Contains non-sensitive user data that can be safely exposed to the client.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    /**
     * Unique identifier for the user.
     */
    private Long id;

    /**
     * Full name of the user.
     */
    private String name;

    /**
     * University email address of the user.
     */
    private String email;

    /**
     * Academic department or major of the user.
     */
    private String branch;

    /**
     * Expected graduation year of the user.
     */
    private Integer graduationYear;

    /**
     * URL to the user's profile picture.
     * Can be null if the user hasn't uploaded a profile picture.
     */
    private String profilePictureUrl;

    /**
     * URL to the user's LinkedIn profile.
     * Can be null if the user hasn't provided it.
     */
    private String linkedinUrl;

    /**
     * URL to the user's GitHub profile.
     * Can be null if the user hasn't provided it.
     */
    private String githubUrl;
}
