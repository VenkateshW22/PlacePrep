package com.vk.placeprep.model; /**
 * Represents an interview experience shared by a user for a specific company and role.
 * Contains details about the interview process, including multiple rounds and final outcome.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "experiences")
public class Experience {
    /**
     * Unique identifier for the experience.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the company where the interview took place.
     */
    private String companyName;

    /**
     * Type of job position (e.g., INTERNSHIP, FULL_TIME).
     */
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    /**
     * Final outcome of the interview process.
     */
    @Enumerated(EnumType.STRING)
    private FinalVerdict finalVerdict;

    /**
     * Flag indicating if the experience is shared anonymously.
     */
    private boolean isAnonymous;

    /**
     * The user who shared this experience.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * List of interview rounds that were part of this experience.
     */
    @OneToMany(mappedBy = "experience", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Round> rounds = new ArrayList<>();

    /**
     * Detailed description of the overall interview experience.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Specific job role or position applied for.
     */
    private String jobRole;

    /**
     * Date when the interview took place.
     */
    private LocalDate interviewDate;

    /**
     * Location where the interview was conducted.
     */
    private String location;

    /**
     * Source of the job opportunity (e.g., LinkedIn, Company Website, Referral).
     */
    private String source;

    /**
     * Time spent preparing for the interview in weeks.
     */
    private Integer preparationTime;

    /**
     * Details about the job offer, if received.
     */
    private String offerDetails;

    /**
     * Description of the overall interview process.
     */
    private String interviewProcess;

    /**
     * Helpful tips for other candidates.
     */
    private String tips;
    /**
     * Helper method to add a round to the experience.
     *
     * @param round The round to be added.
     */
    /**
     * Adds a round to this interview experience and establishes the bidirectional relationship.
     *
     * @param round The round to be added to this experience
     * @throws IllegalArgumentException if the round is null
     */
    public void addRound(Round round) {
        if (round == null) {
            throw new IllegalArgumentException("Round cannot be null");
        }
        if (rounds == null) {
            rounds = new ArrayList<>();
        }
        rounds.add(round);
        round.setExperience(this);
    }

    /**
     * Removes a round from this interview experience.
     *
     * @param round The round to be removed
     */
    public void removeRound(Round round) {
        if (rounds != null && round != null) {
            rounds.remove(round);
            round.setExperience(null);
        }
    }

    /**
     * Gets the total number of rounds in this interview experience.
     *
     * @return The number of rounds, or 0 if no rounds exist
     */
    public int getTotalRounds() {
        return rounds != null ? rounds.size() : 0;
    }

    /**
     * Gets a list of all topics covered across all rounds in this experience.
     *
     * @return A list of unique topics
     */
    public List<String> getAllTopics() {
        if (rounds == null || rounds.isEmpty()) {
            return List.of();
        }
        return rounds.stream()
                .filter(round -> round.getTopicsCovered() != null)
                .flatMap(round -> round.getTopicsCovered().stream())
                .distinct()
                .toList();
    }
}