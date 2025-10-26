package com.vk.placeprep.dto;

import com.vk.placeprep.model.FinalVerdict;
import com.vk.placeprep.model.JobType;
import lombok.Data;
import java.util.List;

/**
 * Data Transfer Object for creating or updating an interview experience.
 * Contains all the necessary information about an interview experience that can be submitted by a user.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
public class ExperienceRequest {
    /**
     * The name of the company where the interview was conducted.
     * This field is required and should not be blank.
     */
    private String companyName;

    /**
     * The job role or position that was being interviewed for.
     * This field is required and should not be blank.
     */
    private String jobRole;

    /**
     * The type of job (e.g., INTERNSHIP, FULL_TIME).
     * This field is required and must be a valid JobType enum value.
     */
    private JobType jobType;

    /**
     * The final outcome of the interview process.
     * This field is required and must be a valid FinalVerdict enum value.
     */
    private FinalVerdict finalVerdict;

    /**
     * Flag indicating whether the experience should be shared anonymously.
     * If true, the user's identity will not be revealed with this experience.
     */
    private boolean isAnonymous;

    /**
     * A detailed description of the overall interview experience.
     * This can include preparation strategies, interview questions, and general advice.
     */
    private String description;

    /**
     * A list of individual rounds that were part of the interview process.
     * Each round should contain details like round type, questions asked, and difficulty.
     */
    private List<RoundRequest> rounds;
}
