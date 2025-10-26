package com.vk.placeprep.service;

import com.vk.placeprep.dto.ExperienceRequest;
import com.vk.placeprep.dto.ExperienceResponse;
import com.vk.placeprep.dto.ExperienceSearchCriteria;
import com.vk.placeprep.dto.ExperienceSummary;
import com.vk.placeprep.dto.RoundResponse;
import com.vk.placeprep.exception.ResourceNotFoundException;
import com.vk.placeprep.exception.UnauthorizedException;
import com.vk.placeprep.model.Difficulty;
import com.vk.placeprep.model.Experience;
import com.vk.placeprep.model.Round;
import com.vk.placeprep.model.User;
import com.vk.placeprep.repository.ExperienceRepository;
import com.vk.placeprep.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing interview experiences.
 * Handles business logic for creating, retrieving, updating, and deleting experiences,
 * as well as searching and generating summaries.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    /**
     * Retrieves an experience by its ID with proper authorization checks.
     * 
     * @param id The ID of the experience to retrieve
     * @param currentUserId The ID of the currently authenticated user (can be null for anonymous access)
     * @return The experience response DTO
     * @throws ResourceNotFoundException if the experience is not found
     * @throws UnauthorizedException if the experience is not anonymous and doesn't belong to the current user
     */
    @Transactional(readOnly = true)
    public ExperienceResponse getExperienceById(Long id, Long currentUserId) {
        Experience experience = experienceRepository.findByIdWithRounds(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));
                
        // Check if the experience is anonymous or belongs to the requesting user
        if (!experience.isAnonymous() && (currentUserId == null || !experience.getUser().getId().equals(currentUserId))) {
            throw new UnauthorizedException("You are not authorized to view this experience");
        }
        
        return toResponse(experience);
    }

    /**
     * Creates a new interview experience for the specified user.
     * 
     * @param request The experience data transfer object containing experience details
     * @param userId The ID of the user creating the experience
     * @return The created experience as a response DTO
     * @throws ResourceNotFoundException if the user is not found
     * @throws IllegalArgumentException if the request is invalid
     */
    @Transactional
    public ExperienceResponse createExperience(ExperienceRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Build the experience entity from the request
        Experience experience = Experience.builder()
                .companyName(request.getCompanyName())
                .jobRole(request.getJobRole())
                .jobType(request.getJobType())
                .finalVerdict(request.getFinalVerdict())
                .isAnonymous(request.isAnonymous())
                .description(request.getDescription())
                .user(user)
                .build();

        // Add all rounds from the request
        if (request.getRounds() != null) {
            request.getRounds().forEach(roundRequest -> {
                Round round = Round.builder()
                        .roundName(roundRequest.getRoundName())
                        .difficulty(roundRequest.getDifficulty())
                        .description(roundRequest.getDescription())
                        .topicsCovered(roundRequest.getTopicsCovered() != null ? 
                                roundRequest.getTopicsCovered() : new ArrayList<>())
                        .build();
                experience.addRound(round);
            });
        }

        Experience savedExperience = experienceRepository.save(experience);
        return toResponse(savedExperience);
    }

    /**
     * Updates an existing interview experience.
     * Only the owner of the experience can update it.
     *
     * @param id The ID of the experience to update
     * @param request The updated experience data
     * @param userId The ID of the user making the request (must be the owner)
     * @return The updated experience as a response DTO
     * @throws ResourceNotFoundException if the experience is not found
     * @throws UnauthorizedException if the user is not the owner of the experience
     */
    @Transactional
    public ExperienceResponse updateExperience(Long id, ExperienceRequest request, Long userId) {
        // Verify the experience exists and belongs to the user
        Experience experience = experienceRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));

        // Update basic experience information
        experience.setCompanyName(request.getCompanyName());
        experience.setJobRole(request.getJobRole());
        experience.setJobType(request.getJobType());
        experience.setFinalVerdict(request.getFinalVerdict());
        experience.setAnonymous(request.isAnonymous());
        experience.setDescription(request.getDescription());

        // Replace all rounds with the new ones from the request
        experience.getRounds().clear();
        if (request.getRounds() != null) {
            request.getRounds().forEach(roundRequest -> {
                Round round = Round.builder()
                        .roundName(roundRequest.getRoundName())
                        .difficulty(roundRequest.getDifficulty())
                        .description(roundRequest.getDescription())
                        .topicsCovered(roundRequest.getTopicsCovered() != null ? 
                                roundRequest.getTopicsCovered() : new ArrayList<>())
                        .build();
                experience.addRound(round);
            });
        }

        Experience updatedExperience = experienceRepository.save(experience);
        return toResponse(updatedExperience);
    }

    /**
     * Deletes an interview experience if the requesting user is the owner.
     * 
     * @param id The ID of the experience to delete
     * @param userId The ID of the user making the request (must be the owner)
     * @throws ResourceNotFoundException if the experience is not found or doesn't belong to the user
     */
    @Transactional
    public void deleteExperience(Long id, Long userId) {
        if (!experienceRepository.existsByIdAndUserId(id, userId)) {
            throw new ResourceNotFoundException("Experience", "id", id);
        }
        experienceRepository.deleteByIdAndUserId(id, userId);
    }

    /**
     * Generates a summary of an interview experience, including statistics about rounds and topics.
     * 
     * @param experienceId The ID of the experience to summarize
     * @param userId The ID of the requesting user (for authorization)
     * @return An ExperienceSummary DTO containing the summarized information
     * @throws ResourceNotFoundException if the experience is not found
     * @throws UnauthorizedException if the experience is not anonymous and doesn't belong to the user
     */
    @Transactional(readOnly = true)
    public ExperienceSummary getExperienceSummary(Long experienceId, Long userId) {
        // Verify the experience exists
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", experienceId));

        // Check if the experience is anonymous or belongs to the requesting user
        if (!experience.isAnonymous() && !experience.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view this experience");
        }

        // Build and return the summary DTO
        return ExperienceSummary.builder()
                .id(experience.getId())
                .companyName(experience.getCompanyName())
                .jobRole(experience.getJobRole())
                .jobType(experience.getJobType().name())
                .finalVerdict(experience.getFinalVerdict().name())
                .roundsCount(experience.getRounds().size())
                .topicsCovered(getAllTopicsCovered(experience))
                .mostCommonDifficulty(getMostCommonDifficulty(experience))
                .build();
    }

    /**
     * Retrieves all unique interview topics covered across all experiences for a specific user.
     *
     * @param userId The ID of the user whose topics to retrieve
     * @return A set of unique topic strings
     */
    @Transactional(readOnly = true)
    public Set<String> getAllTopicsForUser(Long userId) {
        List<Experience> experiences = experienceRepository.findByUserId(userId);
        return experiences.stream()
                .flatMap(exp -> getAllTopicsCovered(exp).stream())
                .collect(Collectors.toSet());
    }

    /**
     * Searches for experiences based on the provided criteria with pagination support.
     * Results can be filtered by company name, job role, job type, final verdict, and more.
     * 
     * @param criteria The search criteria to filter experiences
     * @param pageable Pagination information (page number, size, sorting)
     * @return A page of experience responses matching the criteria
     */
    @Transactional(readOnly = true)
    public Page<ExperienceResponse> searchExperiences(ExperienceSearchCriteria criteria, Pageable pageable) {
        // Clean and prepare search criteria
        if (criteria.getCompanyName() != null) {
            criteria.setCompanyName(criteria.getCompanyName().trim().toLowerCase());
        }
        if (criteria.getJobRole() != null) {
            criteria.setJobRole(criteria.getJobRole().trim().toLowerCase());
        }

        // Build specification
        Specification<Experience> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add search criteria
            if (StringUtils.hasText(criteria.getCompanyName())) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("companyName")), 
                    "%" + criteria.getCompanyName().toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(criteria.getJobRole())) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("jobRole")),
                    "%" + criteria.getJobRole().toLowerCase() + "%"
                ));
            }

            if (criteria.getJobType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("jobType"), criteria.getJobType()));
            }

            if (criteria.getFinalVerdict() != null) {
                predicates.add(criteriaBuilder.equal(root.get("finalVerdict"), criteria.getFinalVerdict()));
            }

            if (criteria.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), criteria.getUserId()));
            }

            if (criteria.getIsAnonymous() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isAnonymous"), criteria.getIsAnonymous()));
            }

            // Only show non-anonymous experiences or those belonging to the current user
            if (criteria.getUserId() == null) {
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("isAnonymous"), false),
                    criteriaBuilder.equal(root.get("user").get("id"), criteria.getUserId())
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Experience> experiences = experienceRepository.findAll(spec, pageable);
        List<ExperienceResponse> responses = experiences.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, experiences.getTotalElements());
    }

    /**
     * Extracts all unique topics covered in an experience's interview rounds.
     * 
     * @param experience The experience to extract topics from
     * @return A set of unique topic strings
     */
    private Set<String> getAllTopicsCovered(Experience experience) {
        return experience.getRounds().stream()
                .filter(round -> round.getTopicsCovered() != null)
                .flatMap(round -> round.getTopicsCovered().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Determines the most common difficulty level among all rounds in an experience.
     * 
     * @param experience The experience to analyze
     * @return The most common difficulty level, or null if no rounds exist
     */
    private Difficulty getMostCommonDifficulty(Experience experience) {
        return experience.getRounds().stream()
                .collect(Collectors.groupingBy(
                    Round::getDifficulty,
                    Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Converts an Experience entity to its corresponding response DTO.
     * Handles null checks and maps nested objects appropriately.
     * 
     * @param experience The experience entity to convert
     * @return The corresponding ExperienceResponse DTO, or null if input is null
     */
    private ExperienceResponse toResponse(Experience experience) {
        if (experience == null) {
            return null;
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
                            .map(this::toRoundResponse)
                            .collect(Collectors.toList()) : 
                        Collections.emptyList())
                .build();
    }

    /**
     * Converts a Round entity to its corresponding response DTO.
     * Handles null checks and maps enum values to their string representations.
     * 
     * @param round The round entity to convert
     * @return The corresponding RoundResponse DTO, or null if input is null
     */
    private RoundResponse toRoundResponse(Round round) {
        if (round == null) {
            return null;
        }
        
        return RoundResponse.builder()
                .id(round.getId())
                .roundName(round.getRoundName())
                .difficulty(round.getDifficulty() != null ? round.getDifficulty().name() : null)
                .description(round.getDescription())
                .topicsCovered(round.getTopicsCovered() != null ? round.getTopicsCovered() : Collections.emptyList())
                .build();
    }
}