package com.vk.placeprep.dto;

import com.vk.placeprep.model.FinalVerdict;
import com.vk.placeprep.model.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for defining search criteria for interview experiences.
 * Allows filtering experiences based on various attributes such as company, job role, type, verdict, and topics.
 * This class is used to capture and validate search parameters for querying interview experiences.
 *
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceSearchCriteria {
    /**
     * Company name to filter experiences by (case-insensitive partial match).
     * If specified, only experiences matching (or containing) this company name will be returned.
     */
    private String companyName;

    /**
     * Job role to filter experiences by (case-insensitive partial match).
     * If specified, only experiences for this job role (or containing the specified text) will be returned.
     */
    private String jobRole;

    /**
     * Job type to filter experiences by (e.g., INTERNSHIP, FULL_TIME).
     * Must match one of the predefined job types in the system.
     */
    private JobType jobType;

    /**
     * Final verdict to filter experiences by (e.g., SELECTED, REJECTED).
     * If specified, only experiences with this final verdict will be returned.
     */
    private FinalVerdict finalVerdict;

    /**
     * List of topic IDs to filter experiences by.
     * An experience must include at least one of the specified topics to be included in the results.
     * If empty or null, no topic-based filtering is applied.
     */
    /**
     * List of topics to filter experiences by.
     * An experience must include at least one of the specified topics to be included in the results.
     * If empty or null, no topic-based filtering is applied.
     */
    private List<String> topics;
    
    /**
     * Difficulty level to filter experiences by.
     * If specified, only experiences with this difficulty level will be returned.
     */
    private String difficulty;
    
    /**
     * User ID to filter experiences by a specific user.
     * If specified, only experiences created by this user will be returned.
     */
    private Long userId;
    
    /**
     * Flag to filter anonymous experiences.
     * If true, only anonymous experiences will be returned.
     * If false, only non-anonymous experiences will be returned.
     * If null, the filter is not applied.
     */
    private Boolean isAnonymous;
}
