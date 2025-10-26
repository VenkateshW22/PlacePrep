package com.vk.placeprep.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an individual interview round within an interview experience.
 * Contains details about the round such as name, difficulty, description, and topics covered.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rounds")
public class Round {

    /**
     * Unique identifier for the round.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the interview round (e.g., "Technical Round 1", "HR Round").
     */
    @Column(nullable = false)
    private String roundName;

    /**
     * The difficulty level of the round.
     */
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    /**
     * Detailed description of the round, including questions asked and experience.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * List of topics covered in this round.
     */
    @ElementCollection
    @CollectionTable(name = "round_topics", joinColumns = @JoinColumn(name = "round_id"))
    @Column(name = "topic")
    private List<String> topicsCovered = new ArrayList<>();

    /**
     * The interview experience this round belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;
}