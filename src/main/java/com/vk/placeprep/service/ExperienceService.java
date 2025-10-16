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
@Service
@RequiredArgsConstructor
@Transactional
public class ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final UserService userService;


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

    @Transactional
    public ExperienceResponse createExperience(ExperienceRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Experience experience = Experience.builder()
                .companyName(request.getCompanyName())
                .jobRole(request.getJobRole())
                .jobType(request.getJobType())
                .finalVerdict(request.getFinalVerdict())
                .isAnonymous(request.isAnonymous())
                .description(request.getDescription())
                .user(user)
                .build();

        request.getRounds().forEach(roundRequest -> {
            Round round = Round.builder()
                    .roundName(roundRequest.getRoundName())
                    .difficulty(roundRequest.getDifficulty())
                    .description(roundRequest.getDescription())
                    .topicsCovered(roundRequest.getTopicsCovered())
                    .build();
            experience.addRound(round);
        });

        Experience savedExperience = experienceRepository.save(experience);
        return toResponse(savedExperience);
    }

    @Transactional
    public ExperienceResponse updateExperience(Long id, ExperienceRequest request, Long userId) {
        Experience experience = experienceRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));

        // Update basic info
        experience.setCompanyName(request.getCompanyName());
        experience.setJobRole(request.getJobRole());
        experience.setJobType(request.getJobType());
        experience.setFinalVerdict(request.getFinalVerdict());
        experience.setAnonymous(request.isAnonymous());
        experience.setDescription(request.getDescription());

        // Update rounds - remove existing ones and add new ones
        experience.getRounds().clear();
        if (request.getRounds() != null) {
            request.getRounds().forEach(roundRequest -> {
                Round round = Round.builder()
                        .roundName(roundRequest.getRoundName())
                        .difficulty(roundRequest.getDifficulty())
                        .description(roundRequest.getDescription())
                        .topicsCovered(roundRequest.getTopicsCovered() != null ? roundRequest.getTopicsCovered() : new ArrayList<>())
                        .build();
                experience.addRound(round);
            });
        }

        Experience updatedExperience = experienceRepository.save(experience);
        return toResponse(updatedExperience);
    }

    @Transactional
    public void deleteExperience(Long id, Long userId) {
        if (!experienceRepository.existsByIdAndUserId(id, userId)) {
            throw new ResourceNotFoundException("Experience", "id", id);
        }
        experienceRepository.deleteByIdAndUserId(id, userId);
    }

    @Transactional(readOnly = true)
    public ExperienceSummary getExperienceSummary(Long experienceId, Long userId) {
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", experienceId));

        // Check if the experience is anonymous or belongs to the requesting user
        if (!experience.isAnonymous() && !experience.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view this experience");
        }

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

    @Transactional(readOnly = true)
    public Set<String> getAllTopicsForUser(Long userId) {
        List<Experience> experiences = experienceRepository.findByUserId(userId);
        return experiences.stream()
                .flatMap(exp -> getAllTopicsCovered(exp).stream())
                .collect(Collectors.toSet());
    }

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

    // Helper methods
    private Set<String> getAllTopicsCovered(Experience experience) {
        return experience.getRounds().stream()
                .filter(round -> round.getTopicsCovered() != null)
                .flatMap(round -> round.getTopicsCovered().stream())
                .collect(Collectors.toSet());
    }

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