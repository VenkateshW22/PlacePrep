package com.vk.placeprep.dto;

import com.vk.placeprep.model.Round;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class RoundResponse {
    private Long id;
    private String roundName;
    private String difficulty;
    private String description;
    private List<String> topicsCovered;

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
