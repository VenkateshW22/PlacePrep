package com.vk.placeprep.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration class for the application.
 * Configures Spring Security settings including authentication, authorization,
 * and security filter chain.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Creates and configures the JWT authentication filter bean.
     * This filter intercepts requests to validate JWT tokens in the Authorization header.
     *
     * @return A new instance of JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Configures the password encoder bean.
     * Uses BCrypt hashing algorithm with strength 10 for password encoding.
     *
     * @return A BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Configures the authentication manager bean.
     * This is used by Spring Security for authenticating users.
     *
     * @param authenticationConfiguration The authentication configuration
     * @return The configured AuthenticationManager
     * @throws Exception If an error occurs while creating the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain that defines the security behavior of the application.
     * 
     * @param http The HttpSecurity to modify
     * @return The configured SecurityFilterChain
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CORS (Cross-Origin Resource Sharing)
                .cors(AbstractHttpConfigurer::disable)
                // Disable CSRF (Cross-Site Request Forgery) as we're using JWT
                .csrf(AbstractHttpConfigurer::disable)
                // Configure session management to be stateless (no HTTP session will be created)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Allow unauthenticated access to authentication endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        // Allow public read access to experiences
                        .requestMatchers(HttpMethod.GET, "/api/experiences/**").permitAll()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                );

        // Add JWT token filter before the default authentication filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
