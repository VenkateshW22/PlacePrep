package com.vk.placeprep.dto;



import com.vk.placeprep.model.FinalVerdict;
import com.vk.placeprep.model.JobType;
import lombok.Data;
import java.util.List;

@Data
public class ExperienceRequest {
    private String companyName;
    private String jobRole;
    private JobType jobType;
    private FinalVerdict finalVerdict;
    private boolean isAnonymous;
    private List<RoundRequest> rounds;
}
