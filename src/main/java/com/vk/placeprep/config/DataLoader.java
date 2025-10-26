package com.vk.placeprep.config;

import com.vk.placeprep.model.Role;
import com.vk.placeprep.model.User;
import com.vk.placeprep.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for initializing application data on startup.
 * This class is responsible for setting up initial data required for the application to function,
 * such as default admin users or other essential data.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Configuration
public class DataLoader {
    
    /**
     * Initializes the database with essential data when the application starts.
     * Creates a default admin user if one doesn't already exist.
     *
     * @param userRepository The repository for user operations
     * @param passwordEncoder The password encoder for hashing passwords
     * @return CommandLineRunner that executes the data initialization
     */
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if not exists
            if (userRepository.findByUniversityEmail("vkadmin@university.edu").isEmpty()) {
                User admin = User.builder()
                        .name("Admin User")
                        .universityEmail("vkadmin@university.edu")
                        .password(passwordEncoder.encode("Admin@123"))
                        .role(Role.ADMIN)
                        .build();
                
                userRepository.save(admin);
                System.out.println("Created admin user: vkadmin@university.edu");
            }
        };
    }
}
