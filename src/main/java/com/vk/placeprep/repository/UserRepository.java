package com.vk.placeprep.repository;

import com.vk.placeprep.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

/**
 * Repository interface for {@link User} entities.
 * Provides CRUD operations and custom queries for user management.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Finds a user by their university email address.
     *
     * @param email The university email address to search for
     * @return An {@link Optional} containing the user if found, or empty if not found
     */
    Optional<User> findByUniversityEmail(String email);
    
    /**
     * Checks if a user with the given university email exists.
     *
     * @param email The university email address to check
     * @return true if a user with the email exists, false otherwise
     */
    Boolean existsByUniversityEmail(String email);
    
    /**
     * Checks if a specific experience belongs to a specific user.
     *
     * @param userId The ID of the user
     * @param experienceId The ID of the experience to check
     * @return true if the experience belongs to the user, false otherwise
     */
    @Query("""
        SELECT CASE WHEN COUNT(e) > 0 
        THEN true ELSE false END 
        FROM User u 
        JOIN u.experiences e 
        WHERE u.id = :userId 
        AND e.id = :experienceId
    """)
    boolean existsByIdAndExperiencesId(
        @Param("userId") Long userId, 
        @Param("experienceId") Long experienceId
    );
    
    /**
     * Checks if a specific experience belongs to the user with the given email.
     *
     * @param email The email of the user
     * @param experienceId The ID of the experience to check
     * @return true if the experience belongs to the user, false otherwise
     */
    @Query("""
        SELECT CASE WHEN COUNT(e) > 0 
        THEN true ELSE false END 
        FROM User u 
        JOIN u.experiences e 
        WHERE u.universityEmail = :email 
        AND e.id = :experienceId
    """)
    boolean existsByUniversityEmailAndExperiencesId(
        @Param("email") String email, 
        @Param("experienceId") Long experienceId
    );
}
