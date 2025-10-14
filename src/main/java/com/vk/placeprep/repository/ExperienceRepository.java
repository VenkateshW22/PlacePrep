package com.vk.placeprep.repository;

import com.vk.placeprep.model.Experience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
        @Query("SELECT e FROM Experience e LEFT JOIN FETCH e.rounds WHERE e.id = :id")
        Optional<Experience> findByIdWithRounds(@Param("id") Long id);

        @Query("SELECT DISTINCT e FROM Experience e LEFT JOIN FETCH e.rounds")
        Page<Experience> findAllWithRounds(Pageable pageable);

}