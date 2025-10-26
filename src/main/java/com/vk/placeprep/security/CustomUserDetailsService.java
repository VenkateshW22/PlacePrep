package com.vk.placeprep.security;

import com.vk.placeprep.model.User;
import com.vk.placeprep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * This service is responsible for loading user details during authentication
 * and is used by Spring Security's authentication manager.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a new CustomUserDetailsService with the specified UserRepository.
     *
     * @param userRepository The repository used to fetch user details
     */
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username (email in this case).
     * This method is called during the authentication process.
     *
     * @param username The username identifying the user whose data is required (email in this case)
     * @return A fully populated UserDetails object (never null)
     * @throws UsernameNotFoundException If the user could not be found or the user has no GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUniversityEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return UserPrincipal.create(user);
    }

    /**
     * Loads a user by their unique ID.
     * This method is typically used for JWT authentication to load user details from a user ID.
     *
     * @param id The ID of the user to load
     * @return A fully populated UserDetails object
     * @throws UsernameNotFoundException If the user with the given ID is not found
     */
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + id)
        );
        return UserPrincipal.create(user);
    }
}
