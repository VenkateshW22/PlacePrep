package com.vk.placeprep.dto;

import com.vk.placeprep.model.Difficulty;
import lombok.Data;
import java.util.List;

@Data
public class RoundRequest {
    private String roundName;
    private Difficulty difficulty;
    private String description;
    private List<String> topicsCovered;
}
