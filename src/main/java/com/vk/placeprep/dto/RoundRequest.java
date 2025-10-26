package com.vk.placeprep.dto;

import com.vk.placeprep.model.Difficulty;
import lombok.Data;
import java.util.List;

/**
 * Data Transfer Object representing a request to create or update an interview round.
 * Contains details about a specific round in the interview process.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
public class RoundRequest {
    /**
     * The name of the interview round (e.g., "Technical Round 1", "HR Round").
     * This field is required and should be unique within an experience.
     */
    private String roundName;
    
    /**
     * The difficulty level of this round.
     * This field is required and must be a valid Difficulty enum value.
     */
    private Difficulty difficulty;
    
    /**
     * A detailed description of the round.
     * This can include the format, duration, and any specific instructions.
     */
    private String description;
    
    /**
     * A list of topics covered in this round.
     * This helps in categorizing and searching for similar interview experiences.
     */
    private List<String> topicsCovered;
}
