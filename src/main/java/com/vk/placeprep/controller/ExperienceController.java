package com.vk.placeprep.controller;

import com.vk.placeprep.dto.ExperienceRequest;
import com.vk.placeprep.dto.ExperienceResponse;
import com.vk.placeprep.dto.ExperienceSearchCriteria;
import com.vk.placeprep.model.Experience;
import com.vk.placeprep.model.FinalVerdict;
import com.vk.placeprep.model.JobType;
import com.vk.placeprep.security.UserPrincipal;
import com.vk.placeprep.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * REST controller for managing interview experiences.
 * Provides endpoints for creating, retrieving, updating, and deleting interview experiences,
 * as well as searching and summarizing experiences.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    /**
     * Searches for interview experiences based on various criteria with pagination support.
     * All parameters are optional and can be combined for filtering.
     *
     * @param companyName   Company name to filter by (case-insensitive partial match)
     * @param jobRole       Job role to filter by (case-insensitive partial match)
     * @param jobType       Type of job (e.g., INTERNSHIP, FULL_TIME)
     * @param finalVerdict  Final outcome of the interview process
     * @param isAnonymous   Filter by anonymous status
     * @param pageable      Pagination information (page, size, sort)
     * @param currentUser   Currently authenticated user (injected by Spring Security)
     * @return A page of experience responses matching the criteria
     */
    @GetMapping
    public Page<ExperienceResponse> searchExperiences(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String jobRole,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String finalVerdict,
            @RequestParam(required = false) Boolean isAnonymous,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        ExperienceSearchCriteria criteria = ExperienceSearchCriteria.builder()
                .companyName(companyName)
                .jobRole(jobRole)
                .jobType(jobType != null ? JobType.valueOf(jobType) : null)
                .finalVerdict(finalVerdict != null ? FinalVerdict.valueOf(finalVerdict) : null)
                .isAnonymous(isAnonymous)
                .userId(currentUser != null ? currentUser.getId() : null)
                .build();

        return experienceService.searchExperiences(criteria, pageable);
    }

    /**
     * Retrieves a specific interview experience by its ID.
     * Access control is applied based on the experience's privacy settings.
     *
     * @param id          The ID of the experience to retrieve
     * @param currentUser Currently authenticated user (injected by Spring Security)
     * @return The requested experience with HTTP 200 OK, or an error response
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> getExperienceById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser != null ? currentUser.getId() : null;
        return ResponseEntity.ok(experienceService.getExperienceById(id, userId));
    }

    /**
     * Creates a new interview experience.
     * The experience will be associated with the currently authenticated user.
     *
     * @param request     The experience data to create
     * @param currentUser Currently authenticated user (injected by Spring Security)
     * @return The created experience with HTTP 200 OK, or an error response
     */
    @PostMapping
    public ResponseEntity<ExperienceResponse> createExperience(
            @Valid @RequestBody ExperienceRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        ExperienceResponse response = experienceService.createExperience(request, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing interview experience.
     * Only the owner of the experience can update it.
     *
     * @param id          The ID of the experience to update
     * @param request     The updated experience data
     * @param currentUser Currently authenticated user (injected by Spring Security)
     * @return The updated experience with HTTP 200 OK, or an error response
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long id,
            @Valid @RequestBody ExperienceRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        ExperienceResponse response = experienceService.updateExperience(id, request, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes an interview experience.
     * Only the owner of the experience can delete it.
     *
     * @param id          The ID of the experience to delete
     * @param currentUser Currently authenticated user (injected by Spring Security)
     * @return HTTP 204 NO_CONTENT on success, or an error response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        experienceService.deleteExperience(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a summary of a specific interview experience.
     * Includes statistics about rounds, topics, and other aggregated data.
     *
     * @param id          The ID of the experience to summarize
     * @param currentUser Currently authenticated user (injected by Spring Security)
     * @return The experience summary with HTTP 200 OK, or an error response
     */
    @GetMapping("/{id}/summary")
    public ResponseEntity<?> getExperienceSummary(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(experienceService.getExperienceSummary(id, currentUser.getId()));
    }

    /**
     * Retrieves all unique interview topics from the current user's experiences.
     *
     * @param currentUser Currently authenticated user (injected by Spring Security)
     * @return A set of unique topic strings with HTTP 200 OK
     */
    @GetMapping("/topics")
    public ResponseEntity<Set<String>> getAllTopics(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(experienceService.getAllTopicsForUser(currentUser.getId()));
    }

    /**
     * Retrieves a paginated list of the current user's interview experiences.
     * Can be filtered by interview status.
     *
     * @param status      Optional status to filter by (e.g., OFFER_ACCEPTED, REJECTED)
     * @param pageable    Pagination information (page, size, sort)
     * @param currentUser Currently authenticated user (injected by Spring Security)
     * @return A page of the user's experiences with HTTP 200 OK
     */
    @GetMapping("/me")
    public ResponseEntity<Page<ExperienceResponse>> getUserExperiences(
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        ExperienceSearchCriteria criteria = ExperienceSearchCriteria.builder()
                .userId(currentUser.getId())
                .finalVerdict(status != null ? FinalVerdict.valueOf(status) : null)
                .build();
                
        return ResponseEntity.ok(experienceService.searchExperiences(criteria, pageable));
    }
}