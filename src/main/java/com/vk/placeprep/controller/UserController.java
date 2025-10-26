package com.vk.placeprep.controller;

import com.vk.placeprep.dto.UpdateProfileRequest;
import com.vk.placeprep.dto.UserProfileResponse;
import com.vk.placeprep.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing user profiles and related operations.
 * Provides endpoints for retrieving and updating user profiles, as well as accessing
 * user-specific data like interview experience summaries.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves the profile of the currently authenticated user.
     *
     * @param userDetails The authentication details of the current user (injected by Spring Security)
     * @return The user's profile information with HTTP 200 OK
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserProfileResponse profile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    /**
     * Updates the profile of the currently authenticated user.
     * Only the user can update their own profile.
     *
     * @param userDetails The authentication details of the current user (injected by Spring Security)
     * @param request The updated profile information
     * @return The updated user profile with HTTP 200 OK
     */
    @PutMapping("/update/me")
    public ResponseEntity<UserProfileResponse> updateCurrentUserProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse updatedProfile = userService.updateUserProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Retrieves a summary of the current user's interview experiences.
     * Includes statistics and aggregated data about the user's interview history.
     *
     * @param userDetails The authentication details of the current user (injected by Spring Security)
     * @return A summary of the user's interview experiences with HTTP 200 OK
     */
    @GetMapping("/me/experiences/summary")
    public ResponseEntity<?> getUserExperienceSummary(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserExperienceSummary(userDetails.getUsername()));
    }
}
