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

public interface ExperienceRepository extends JpaRepository<Experience, Long>, JpaSpecificationExecutor<Experience> {
        @Query("SELECT e FROM Experience e LEFT JOIN FETCH e.rounds WHERE e.id = :id")
        Optional<Experience> findByIdWithRounds(@Param("id") Long id);

        @Query("SELECT DISTINCT e FROM Experience e LEFT JOIN FETCH e.rounds")
        Page<Experience> findAllWithRounds(Pageable pageable);
        
        @Query("SELECT e FROM Experience e WHERE e.id = :id AND e.user.id = :userId")
        Optional<Experience> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

        @Query("SELECT e FROM Experience e LEFT JOIN FETCH e.rounds WHERE e.user.id = :userId")
        List<Experience> findByUserId(@Param("userId") Long userId);

    // In ExperienceRepository.java

    @Query("SELECT e FROM Experience e WHERE " +
            "(:companyName IS NULL OR LOWER(e.companyName) LIKE LOWER(concat('%', :companyName,'%'))) AND " +
            "(:jobRole IS NULL OR LOWER(e.jobRole) LIKE LOWER(concat('%', :jobRole,'%'))) AND " +
            "(:jobType IS NULL OR e.jobType = :jobType) AND " +
            "(:finalVerdict IS NULL OR e.finalVerdict = :finalVerdict)")
    Page<Experience> searchExperiences(
            @Param("companyName") String companyName,
            @Param("jobRole") String jobRole,
            @Param("jobType") JobType jobType,
            @Param("finalVerdict") FinalVerdict finalVerdict,
            Pageable pageable
    );

        void deleteByIdAndUserId(Long id, Long userId);

        boolean existsByIdAndUserId(Long id, Long userId);
        
        @Query("SELECT e FROM Experience e LEFT JOIN FETCH e.rounds WHERE e.user = :user")
        List<Experience> findByUser(@Param("user") User user);
}