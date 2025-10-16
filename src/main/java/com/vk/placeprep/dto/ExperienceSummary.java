package com.vk.placeprep.dto;

import com.vk.placeprep.model.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceSummary {
    private Long id;
    private String companyName;
    private String jobRole;
    private String jobType;
    private String finalVerdict;
    private int roundsCount;
    private Set<String> topicsCovered;
    private Difficulty mostCommonDifficulty;
}
