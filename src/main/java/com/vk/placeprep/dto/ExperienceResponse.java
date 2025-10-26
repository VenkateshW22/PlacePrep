package com.vk.placeprep.dto;

import com.vk.placeprep.model.Experience;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object representing an interview experience in API responses.
 * Contains all the details of an interview experience that can be safely exposed to clients.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceResponse {
    /**
     * Unique identifier for the experience.
     */
    private Long id;

    /**
     * ID of the user who shared this experience.
     * May be null if the experience is shared anonymously.
     */
    private Long userId;

    /**
     * Name of the company where the interview was conducted.
     */
    private String companyName;

    /**
     * Job role or position that was being interviewed for.
     */
    private String jobRole;

    /**
     * Type of job (e.g., INTERNSHIP, FULL_TIME).
     */
    private String jobType;

    /**
     * Final outcome of the interview process.
     */
    private String finalVerdict;

    /**
     * Detailed description of the interview experience.
     */
    private String description;

    /**
     * Flag indicating if the experience is shared anonymously.
     */
    private boolean isAnonymous;

    /**
     * List of interview rounds that were part of this experience.
     */
    private List<RoundResponse> rounds;

    /**
     * Converts an Experience entity to an ExperienceResponse DTO.
     *
     * @param experience The Experience entity to convert
     * @return A new ExperienceResponse instance populated with data from the entity
     * @throws IllegalArgumentException if the experience parameter is null
     */
    public static ExperienceResponse fromEntity(Experience experience) {
        if (experience == null) {
            throw new IllegalArgumentException("Experience cannot be null");
        }
        
        return ExperienceResponse.builder()
                .id(experience.getId())
                .userId(experience.getUser() != null ? experience.getUser().getId() : null)
                .companyName(experience.getCompanyName())
                .jobRole(experience.getJobRole())
                .jobType(experience.getJobType() != null ? experience.getJobType().name() : null)
                .finalVerdict(experience.getFinalVerdict() != null ? experience.getFinalVerdict().name() : null)
                .isAnonymous(experience.isAnonymous())
                .description(experience.getDescription())
                .rounds(experience.getRounds() != null ? 
                        experience.getRounds().stream()
                            .map(RoundResponse::fromEntity)
                            .collect(Collectors.toList()) : 
                        List.of())
                .build();
    }
}

