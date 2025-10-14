package com.vk.placeprep.repository;


import com.vk.placeprep.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUniversityEmail(String email);
    Boolean existsByUniversityEmail(String email);
}
