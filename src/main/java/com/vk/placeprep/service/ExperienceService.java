package com.vk.placeprep.service;

import com.vk.placeprep.dto.ExperienceRequest;
import com.vk.placeprep.exception.ResourceNotFoundException;
import com.vk.placeprep.model.Experience;
import com.vk.placeprep.model.Round;
import com.vk.placeprep.model.User;
import com.vk.placeprep.repository.ExperienceRepository;
import com.vk.placeprep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class ExperienceService {
    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<Experience> getAllExperiences(Pageable pageable) {
        return experienceRepository.findAllWithRounds(pageable);
    }

    @Transactional(readOnly = true)
    public Experience getExperienceById(Long id) {
        return experienceRepository.findByIdWithRounds(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));
    }

    public Experience createExperience(ExperienceRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Experience experience = Experience.builder()
                .companyName(request.getCompanyName())
                .jobRole(request.getJobRole())
                .jobType(request.getJobType())
                .finalVerdict(request.getFinalVerdict())
                .isAnonymous(request.isAnonymous())
                .user(user)
                .build();

        request.getRounds().forEach(roundRequest -> {
            Round round = Round.builder()
                    .roundName(roundRequest.getRoundName())
                    .difficulty(roundRequest.getDifficulty())
                    .description(roundRequest.getDescription())
                    .topicsCovered(roundRequest.getTopicsCovered())
                    .experience(experience)
                    .build();
            experience.getRounds().add(round);
        });

        return experienceRepository.save(experience);
    }

    public Experience updateExperience(Long id, ExperienceRequest request, Long id1) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));

        experience.setCompanyName(request.getCompanyName());
        experience.setJobRole(request.getJobRole());
        experience.setJobType(request.getJobType());
        experience.setFinalVerdict(request.getFinalVerdict());
        experience.setAnonymous(request.isAnonymous());

        return experienceRepository.save(experience);
    }
}