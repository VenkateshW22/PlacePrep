package com.vk.placeprep.config;

import com.vk.placeprep.model.Role;
import com.vk.placeprep.model.User;
import com.vk.placeprep.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if not exists
            if (userRepository.findByUniversityEmail("vkadmin@university.edu").isEmpty()) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setUniversityEmail("vkadmin@university.edu");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Created admin user: vkadmin@university.edu");
            }

        };
    }
}
