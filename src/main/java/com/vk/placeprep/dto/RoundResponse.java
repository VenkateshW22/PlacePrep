package com.vk.placeprep.dto;

import com.vk.placeprep.model.Round;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object representing an interview round in API responses.
 * Contains all the details of an interview round that can be safely exposed to clients.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundResponse {
    /**
     * The unique identifier of the round.
     */
    private Long id;
    
    /**
     * The name of the interview round.
     */
    private String roundName;
    
    /**
     * The difficulty level of this round as a string.
     * This is the string representation of the Difficulty enum.
     */
    private String difficulty;
    
    /**
     * A detailed description of the round.
     */
    private String description;
    
    /**
     * A list of topics covered in this round.
     */
    private List<String> topicsCovered;

    /**
     * Creates a RoundResponse from a Round entity.
     * 
     * @param round The Round entity to convert
     * @return A new RoundResponse instance with data from the entity
     * @throws IllegalArgumentException if the round parameter is null
     */
    public static RoundResponse fromEntity(Round round) {
        return RoundResponse.builder()
                .id(round.getId())
                .roundName(round.getRoundName())
                .difficulty(round.getDifficulty().name())
                .description(round.getDescription())
                .topicsCovered(round.getTopicsCovered())
                .build();
    }
}
