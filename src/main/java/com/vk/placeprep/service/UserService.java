package com.vk.placeprep.service;

import com.vk.placeprep.dto.UpdateProfileRequest;
import com.vk.placeprep.dto.UserProfileResponse;
import com.vk.placeprep.dto.UserExperienceSummary;
import com.vk.placeprep.exception.ResourceNotFoundException;
import com.vk.placeprep.model.Experience;
import com.vk.placeprep.model.FinalVerdict;
import com.vk.placeprep.model.JobType;
import com.vk.placeprep.model.User;
import com.vk.placeprep.repository.ExperienceRepository;
import com.vk.placeprep.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ExperienceRepository experienceRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(String email) {
        User user = userRepository.findByUniversityEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getUniversityEmail())
                .branch(user.getBranch())
                .graduationYear(user.getGraduationYear())
                .profilePictureUrl(user.getProfilePictureUrl())
                .linkedinUrl(user.getLinkedinUrl())
                .githubUrl(user.getGithubUrl())
                .build();
    }

    @Transactional
    public UserProfileResponse updateUserProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByUniversityEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // Update basic profile information
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        
        if (request.getBranch() != null) {
            user.setBranch(request.getBranch());
        }
        
        if (request.getGraduationYear() != null) {
            user.setGraduationYear(request.getGraduationYear());
        }
        
        // Update profile URLs if provided
        if (request.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(request.getProfilePictureUrl());
        }
        
        if (request.getLinkedinUrl() != null) {
            user.setLinkedinUrl(request.getLinkedinUrl());
        }
        
        if (request.getGithubUrl() != null) {
            user.setGithubUrl(request.getGithubUrl());
        }

        // Handle password change if requested
        if (request.getCurrentPassword() != null && !request.getCurrentPassword().isBlank() &&
            request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
            
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        User updatedUser = userRepository.save(user);
        
        return UserProfileResponse.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getUniversityEmail())
                .branch(updatedUser.getBranch())
                .graduationYear(updatedUser.getGraduationYear())
                .profilePictureUrl(updatedUser.getProfilePictureUrl())
                .linkedinUrl(updatedUser.getLinkedinUrl())
                .githubUrl(updatedUser.getGithubUrl())
                .build();
    }

    @Transactional(readOnly = true)
    public boolean isUserOwnerOfExperience(String email, Long experienceId) {
        return userRepository.existsByUniversityEmailAndExperiencesId(email, experienceId);
    }

    @Transactional(readOnly = true)
    public UserExperienceSummary getUserExperienceSummary(String email) {
        // Get user by email
        User user = userRepository.findByUniversityEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // Get all experiences for the user
        List<Experience> experiences = experienceRepository.findByUser(user);
        
        // Calculate basic statistics
        int totalApplications = experiences.size();
        
        // Count by final verdict
        Map<FinalVerdict, Long> verdictCounts = experiences.stream()
                .collect(Collectors.groupingBy(Experience::getFinalVerdict, Collectors.counting()));
        
        // Count by job type
        Map<JobType, Long> jobTypeCounts = experiences.stream()
                .collect(Collectors.groupingBy(Experience::getJobType, Collectors.counting()));
        
        // Count by company
        Map<String, Long> companyCounts = experiences.stream()
                .collect(Collectors.groupingBy(
                    exp -> {
                        String companyName = exp.getCompanyName();
                        return companyName != null ? companyName.toLowerCase() : "unknown";
                    },
                    Collectors.counting()
                ));
        
        // Calculate round statistics
        int totalRounds = experiences.stream()
                .mapToInt(exp -> exp.getRounds().size())
                .sum();
        
        double averageRounds = totalApplications > 0 ? (double) totalRounds / totalApplications : 0;
        
        // Calculate success rate (OFFER_ACCEPTED / (total - PENDING))
        long pendingCount = verdictCounts.getOrDefault(FinalVerdict.PENDING, 0L);
        long completedApplications = totalApplications - pendingCount;
        long successfulApplications = verdictCounts.getOrDefault(FinalVerdict.OFFER_ACCEPTED, 0L);
        
        double successRate = completedApplications > 0 
                ? (double) successfulApplications / completedApplications * 100 
                : 0;

        // Get most common interview topics
        Map<String, Long> topicFrequency = experiences.stream()
                .flatMap(exp -> exp.getRounds().stream())
                .filter(round -> round.getTopicsCovered() != null)
                .flatMap(round -> round.getTopicsCovered().stream())
                .filter(topic -> topic != null)
                .collect(Collectors.groupingBy(
                    String::toLowerCase,
                    Collectors.counting()
                ));
        
        // Get top 5 most common topics
        List<String> topTopics = topicFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Build and return the summary
        return UserExperienceSummary.builder()
                .totalApplications(totalApplications)
                .applicationsByStatus(verdictCounts.entrySet().stream()
                        .collect(Collectors.toMap(
                            entry -> entry.getKey().name(),
                            Map.Entry::getValue
                        )))
                .applicationsByJobType(jobTypeCounts.entrySet().stream()
                        .collect(Collectors.toMap(
                            entry -> entry.getKey().name(),
                            Map.Entry::getValue
                        )))
                .topCompanies(companyCounts.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(5)
                        .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                        )))
                .totalRounds(totalRounds)
                .averageRoundsPerApplication(Math.round(averageRounds * 100.0) / 100.0)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .topInterviewTopics(topTopics)
                .build();
    }
}
