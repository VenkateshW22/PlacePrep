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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider tokenProvider;

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