package com.vk.placeprep.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder(builderClassName = "UserExperienceSummaryBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class UserExperienceSummary {
    private int totalApplications;
    private Map<String, Long> applicationsByStatus;
    private Map<String, Long> applicationsByJobType;
    private Map<String, Long> topCompanies;
    private int totalRounds;
    private double averageRoundsPerApplication;
    private double successRate;
    private List<String> topInterviewTopics;
    
    // Custom builder to handle null values
    public static class UserExperienceSummaryBuilder {
        private Map<String, Long> applicationsByStatus = new LinkedHashMap<>();
        private Map<String, Long> applicationsByJobType = new LinkedHashMap<>();
        private Map<String, Long> topCompanies = new LinkedHashMap<>();
        private List<String> topInterviewTopics = new ArrayList<>();
    }
    
    // Ensure non-null collections
    public Map<String, Long> getApplicationsByStatus() {
        return applicationsByStatus != null ? applicationsByStatus : new LinkedHashMap<>();
    }
    
    public Map<String, Long> getApplicationsByJobType() {
        return applicationsByJobType != null ? applicationsByJobType : new LinkedHashMap<>();
    }
    
    public Map<String, Long> getTopCompanies() {
        return topCompanies != null ? topCompanies : new LinkedHashMap<>();
    }
    
    public List<String> getTopInterviewTopics() {
        return topInterviewTopics != null ? topInterviewTopics : List.of();
    }
}
