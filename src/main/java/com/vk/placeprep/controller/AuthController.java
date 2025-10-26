package com.vk.placeprep.controller;

import com.vk.placeprep.dto.ApiResponse;
import com.vk.placeprep.dto.JwtAuthenticationResponse;
import com.vk.placeprep.dto.LoginRequest;
import com.vk.placeprep.dto.SignUpRequest;
import com.vk.placeprep.model.Role;
import com.vk.placeprep.model.User;
import com.vk.placeprep.repository.UserRepository;
import com.vk.placeprep.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling authentication-related requests.
 * Provides endpoints for user registration and login.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    /**
     * Constructs a new AuthController with the required dependencies.
     *
     * @param authenticationManager The authentication manager
     * @param userRepository The user repository
     * @param passwordEncoder The password encoder
     * @param tokenProvider The JWT token provider
     */
    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder,
                         JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Authenticates a user and returns a JWT token upon successful authentication.
     *
     * @param loginRequest The login request containing user credentials
     * @return ResponseEntity containing a JWT token if authentication is successful,
     *         or an error response if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUniversityEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    /**
     * Registers a new user account.
     *
     * @param signUpRequest The sign-up request containing user details
     * @return ResponseEntity indicating success or failure of user registration
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUniversityEmail(signUpRequest.getUniversityEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .name(signUpRequest.getName())
                .universityEmail(signUpRequest.getUniversityEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .branch(signUpRequest.getBranch())
                .graduationYear(signUpRequest.getGraduationYear())
                .role(Role.USER) // Default role
                .build();

        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }
}