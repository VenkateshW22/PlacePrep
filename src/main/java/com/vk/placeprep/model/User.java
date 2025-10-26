package com.vk.placeprep.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user of the PlacePrep application.
 * Contains user profile information, authentication details, and relationships to interview experiences.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "universityEmail")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the user.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * URL to the user's profile picture.
     */
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    /**
     * URL to the user's LinkedIn profile.
     */
    @Column(name = "linkedin_url")
    private String linkedinUrl;

    /**
     * URL to the user's GitHub profile.
     */
    @Column(name = "github_url")
    private String githubUrl;

    /**
     * University email address of the user. Used for authentication.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String universityEmail;

    /**
     * The year the user is expected to graduate.
     */
    private Integer graduationYear;

    /**
     * Hashed password for user authentication.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
    private String password;

    /**
     * Academic department or major of the user.
     */
    private String branch;

    /**
     * Timestamp when the user account was created.
     */
    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Timestamp when the user account was last updated.
     */
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Role of the user in the system (e.g., USER, ADMIN).
     */
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    /**
     * List of interview experiences shared by the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Experience> experiences = new ArrayList<>();

    /**
     * Adds an interview experience to the user's profile and establishes the bidirectional relationship.
     *
     * @param experience The experience to be added to the user's profile
     * @throws IllegalArgumentException if the experience is null
     */
    public void addExperience(Experience experience) {
        if (experience == null) {
            throw new IllegalArgumentException("Experience cannot be null");
        }
        experiences.add(experience);
        experience.setUser(this);
    }

    /**
     * Removes an interview experience from the user's profile.
     *
     * @param experience The experience to be removed
     */
    public void removeExperience(Experience experience) {
        if (experience != null) {
            experiences.remove(experience);
            experience.setUser(null);
        }
    }

    /**
     * Gets all interview experiences associated with this user.
     *
     * @return An unmodifiable list of experiences
     */
    public List<Experience> getExperiences() {
        return List.copyOf(experiences);
    }

    /**
     * Replaces the current list of experiences with the provided list.
     * Note: This will clear existing experiences and establish new relationships.
     *
     * @param experiences The new list of experiences
     */
    public void setExperiences(List<Experience> experiences) {
        if (experiences == null) {
            this.experiences.clear();
            return;
        }
        
        this.experiences.clear();
        this.experiences.addAll(experiences);
        experiences.forEach(exp -> exp.setUser(this));
    }

}
