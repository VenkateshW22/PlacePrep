package com.vk.placeprep.dto;

import com.vk.placeprep.model.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Transfer Object representing a summary of an interview experience.
 * Provides an overview of key details about an interview experience
 * without including the full details of each round.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceSummary {
    /**
     * Unique identifier for the experience.
     */
    private Long id;
    
    /**
     * The name of the company where the interview was conducted.
     */
    private String companyName;
    
    /**
     * The job role or position that was being interviewed for.
     */
    private String jobRole;
    
    /**
     * The type of job (e.g., INTERNSHIP, FULL_TIME).
     */
    private String jobType;
    
    /**
     * The final outcome of the interview process (e.g., SELECTED, REJECTED).
     */
    private String finalVerdict;
    
    /**
     * The total number of interview rounds in this experience.
     */
    private int roundsCount;
    
    /**
     * A set of topics covered across all rounds of this interview experience.
     * Useful for filtering and searching experiences by topic.
     */
    private Set<String> topicsCovered;
    
    /**
     * The most common difficulty level across all rounds of this interview.
     * Helps users understand the overall challenge level of the interview process.
     */
    private Difficulty mostCommonDifficulty;
}
