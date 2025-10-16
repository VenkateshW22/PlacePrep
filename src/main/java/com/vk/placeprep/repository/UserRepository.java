package com.vk.placeprep.repository;


import com.vk.placeprep.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUniversityEmail(String email);
    Boolean existsByUniversityEmail(String email);
    
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM User u JOIN u.experiences e WHERE u.id = :userId AND e.id = :experienceId")
    boolean existsByIdAndExperiencesId(@Param("userId") Long userId, @Param("experienceId") Long experienceId);
    
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM User u JOIN u.experiences e WHERE u.universityEmail = :email AND e.id = :experienceId")
    boolean existsByUniversityEmailAndExperiencesId(@Param("email") String email, @Param("experienceId") Long experienceId);
}
