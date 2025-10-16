package com.vk.placeprep.dto;

import com.vk.placeprep.model.FinalVerdict;
import com.vk.placeprep.model.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceSearchCriteria {
    private String companyName;
    private String jobRole;
    private JobType jobType;
    private FinalVerdict finalVerdict;
    private List<String> topics;
    private String difficulty;
    private Long userId;
    private Boolean isAnonymous;
}
