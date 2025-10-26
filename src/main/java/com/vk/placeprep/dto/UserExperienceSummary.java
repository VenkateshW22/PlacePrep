package com.vk.placeprep.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object representing aggregated statistics about a user's interview experiences.
 * Provides a comprehensive overview of the user's interview history and performance metrics.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
@Builder(builderClassName = "UserExperienceSummaryBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class UserExperienceSummary {
    /**
     * The total number of job applications submitted by the user.
     */
    private int totalApplications;
    
    /**
     * A map of application statuses to their respective counts.
     * Keys represent statuses (e.g., "APPLIED", "INTERVIEW", "OFFER", "REJECTED").
     * Values represent the count of applications in each status.
     */
    private Map<String, Long> applicationsByStatus;
    
    /**
     * A map of job types to their respective application counts.
     * Keys represent job types (e.g., "INTERNSHIP", "FULL_TIME").
     * Values represent the count of applications for each job type.
     */
    private Map<String, Long> applicationsByJobType;
    
    /**
     * A map of company names to the number of applications submitted to each.
     * Sorted by frequency in descending order.
     */
    private Map<String, Long> topCompanies;
    
    /**
     * The total number of interview rounds completed across all applications.
     */
    private int totalRounds;
    
    /**
     * The average number of interview rounds per application.
     * Calculated as totalRounds / totalApplications.
     */
    private double averageRoundsPerApplication;
    
    /**
     * The user's success rate in interviews, represented as a decimal between 0 and 1.
     * Calculated as (number of successful applications / total applications).
     */
    private double successRate;
    
    /**
     * A list of the most frequently encountered interview topics.
     * Sorted by frequency in descending order.
     */
    private List<String> topInterviewTopics;
    
    /**
     * Custom builder implementation for UserExperienceSummary.
     * Initializes collections to prevent null pointer exceptions.
     */
    public static class UserExperienceSummaryBuilder {
        /** Map to track application counts by status */
        private Map<String, Long> applicationsByStatus = new LinkedHashMap<>();
        
        /** Map to track application counts by job type */
        private Map<String, Long> applicationsByJobType = new LinkedHashMap<>();
        
        /** Map to track application counts by company */
        private Map<String, Long> topCompanies = new LinkedHashMap<>();
        
        /** List of most common interview topics */
        private List<String> topInterviewTopics = new ArrayList<>();
    }
    
    /**
     * Returns the applications by status map, ensuring it's never null.
     * 
     * @return A non-null map of application statuses to their counts
     */
    public Map<String, Long> getApplicationsByStatus() {
        return applicationsByStatus != null ? applicationsByStatus : new LinkedHashMap<>();
    }
    
    /**
     * Returns the applications by job type map, ensuring it's never null.
     * 
     * @return A non-null map of job types to their application counts
     */
    public Map<String, Long> getApplicationsByJobType() {
        return applicationsByJobType != null ? applicationsByJobType : new LinkedHashMap<>();
    }
    
    /**
     * Returns the top companies map, ensuring it's never null.
     * 
     * @return A non-null map of company names to application counts
     */
    public Map<String, Long> getTopCompanies() {
        return topCompanies != null ? topCompanies : new LinkedHashMap<>();
    }
    
    /**
     * Returns the list of top interview topics, ensuring it's never null.
     * 
     * @return A non-null list of interview topics
     */
    public List<String> getTopInterviewTopics() {
        return topInterviewTopics != null ? topInterviewTopics : List.of();
    }
}
