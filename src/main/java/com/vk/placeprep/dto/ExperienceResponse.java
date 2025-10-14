package com.vk.placeprep.dto;

import com.vk.placeprep.model.Experience;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceResponse {
    private Long id;
    private Long userId;
    private String companyName;
    private String jobRole;
    private String jobType;
    private String finalVerdict;
    private String description;
    private boolean isAnonymous;
    private List<RoundResponse> rounds;

    public static ExperienceResponse fromEntity(Experience experience) {
        return ExperienceResponse.builder()
                .id(experience.getId())
                .userId(experience.getUser().getId())
                .companyName(experience.getCompanyName())
                .jobRole(experience.getJobRole())
                .jobType(experience.getJobType().name())
                .finalVerdict(experience.getFinalVerdict() != null ? experience.getFinalVerdict().name() : null)
                .isAnonymous(experience.isAnonymous())
                .description(experience.getDescription())
                .rounds(experience.getRounds().stream()
                        .map(RoundResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}

