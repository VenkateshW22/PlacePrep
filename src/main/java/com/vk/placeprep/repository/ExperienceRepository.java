package com.vk.placeprep.repository;

import com.vk.placeprep.model.Experience;
import com.vk.placeprep.model.FinalVerdict;
import com.vk.placeprep.model.User;
import com.vk.placeprep.model.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Experience} entities.
 * Provides custom queries for retrieving and managing interview experiences.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
public interface ExperienceRepository extends JpaRepository<Experience, Long>, JpaSpecificationExecutor<Experience> {
    
    /**
     * Finds an experience by its ID and eagerly fetches its associated rounds.
     *
     * @param id The ID of the experience to find
     * @return An {@link Optional} containing the experience if found, or empty if not found
     */
    @Query("""
        SELECT e 
        FROM Experience e 
        LEFT JOIN FETCH e.rounds 
        WHERE e.id = :id
    """)
    Optional<Experience> findByIdWithRounds(@Param("id") Long id);

    /**
     * Finds all experiences with their associated rounds, with pagination support.
     *
     * @param pageable Pagination information
     * @return A page of experiences with their rounds
     */
    @Query("""
        SELECT DISTINCT e 
        FROM Experience e 
        LEFT JOIN FETCH e.rounds
    """)
    Page<Experience> findAllWithRounds(Pageable pageable);
    
    /**
     * Finds an experience by its ID and the ID of the user who owns it.
     *
     * @param id The ID of the experience to find
     * @param userId The ID of the user who owns the experience
     * @return An {@link Optional} containing the experience if found and owned by the user
     */
    @Query("""
        SELECT e 
        FROM Experience e 
        WHERE e.id = :id 
        AND e.user.id = :userId
    """)
    Optional<Experience> findByIdAndUserId(
        @Param("id") Long id, 
        @Param("userId") Long userId
    );

    /**
     * Finds all experiences for a specific user, including their rounds.
     *
     * @param userId The ID of the user
     * @return A list of experiences with their rounds for the specified user
     */
    @Query("""
        SELECT e 
        FROM Experience e 
        LEFT JOIN FETCH e.rounds 
        WHERE e.user.id = :userId
    """)
    List<Experience> findByUserId(@Param("userId") Long userId);

    /**
     * Searches for experiences based on various criteria with pagination support.
     * All parameters are optional - if a parameter is null, it is not included in the search criteria.
     *
     * @param companyName  The company name to search for (case-insensitive partial match)
     * @param jobRole      The job role to search for (case-insensitive partial match)
     * @param jobType      The type of job to filter by
     * @param finalVerdict The final verdict to filter by
     * @param pageable     Pagination information
     * @return A page of experiences matching the search criteria
     */
    @Query("""
        SELECT e 
        FROM Experience e 
        WHERE (:companyName IS NULL OR LOWER(e.companyName) LIKE LOWER(concat('%', :companyName,'%'))) 
        AND (:jobRole IS NULL OR LOWER(e.jobRole) LIKE LOWER(concat('%', :jobRole,'%'))) 
        AND (:jobType IS NULL OR e.jobType = :jobType) 
        AND (:finalVerdict IS NULL OR e.finalVerdict = :finalVerdict)
    """)
    Page<Experience> searchExperiences(
        @Param("companyName") String companyName,
        @Param("jobRole") String jobRole,
        @Param("jobType") JobType jobType,
        @Param("finalVerdict") FinalVerdict finalVerdict,
        Pageable pageable
    );

    /**
     * Deletes an experience by its ID and the ID of the user who owns it.
     *
     * @param id     The ID of the experience to delete
     * @param userId The ID of the user who owns the experience
     */
    void deleteByIdAndUserId(Long id, Long userId);

    /**
     * Checks if an experience with the given ID exists and is owned by the specified user.
     *
     * @param id     The ID of the experience to check
     * @param userId The ID of the user to verify ownership
     * @return true if the experience exists and is owned by the user, false otherwise
     */
    boolean existsByIdAndUserId(Long id, Long userId);
    
    /**
     * Finds all experiences for a specific user, including their rounds.
     *
     * @param user The user entity
     * @return A list of experiences with their rounds for the specified user
     */
    @Query("""
        SELECT e 
        FROM Experience e 
        LEFT JOIN FETCH e.rounds 
        WHERE e.user = :user
    """)
    List<Experience> findByUser(@Param("user") User user);
}