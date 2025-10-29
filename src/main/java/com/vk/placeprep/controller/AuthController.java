package com.vk.placeprep.controller;
import com.vk.placeprep.dto.ApiResponse;
import com.vk.placeprep.dto.JwtAuthenticationResponse;
import com.vk.placeprep.dto.LoginRequest;
import com.vk.placeprep.dto.SignUpRequest;
import com.vk.placeprep.model.Role;
import com.vk.placeprep.model.User;
import com.vk.placeprep.service.EmailService;
import com.vk.placeprep.util.otpUtil;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    @Autowired
    private otpUtil otpUtil;
    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByUniversityEmail(loginRequest.getUniversityEmail());

        if(optionalUser.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(false, "User not found , Please register first "),
                    HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUniversityEmail(),
                        loginRequest.getPassword()
                )
        );
        String otp = otpUtil.generateOtp(user.getUniversityEmail());
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        emailService.sendOtpEmail(user.getUniversityEmail() , user.getOtp());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = tokenProvider.generateToken(authentication);
        String message = String.format(
                "An OTP has been sent to your registered email address: %s. Please verify to complete your login.",
                user.getUniversityEmail()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
//        response.put("token", jwt);

        // Step 6: Return combined response
        return ResponseEntity.ok(response);//please check the resonse is correct or not
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
    @PostMapping("/login")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String , String> request)
    {
        String universityMail = request.get("universityEmail");
        String enteredOtp = request.get("otp");
        Optional<User> optionalUser = userRepository.findByUniversityEmail(universityMail);
        if(optionalUser.isEmpty()) {
            return  new ResponseEntity<>(new ApiResponse(false, "user not found"),HttpStatus.BAD_REQUEST);
        }
        User user = optionalUser.get();
        if(user.getOtp()== null || user.getOtp().equals(enteredOtp)) {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid OTP."), HttpStatus.BAD_REQUEST);
        }
        if(user.getOtpGeneratedTime() == null || user.getOtpGeneratedTime().plusMinutes(5).isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid OTP."), HttpStatus.BAD_REQUEST);
        }
        user.setOtp(null);
        user.setOtpGeneratedTime(null);
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUniversityEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        Map<String , Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "OTP verified successfully. Login complete!");
        response.put("token", jwt);
        return ResponseEntity.ok(response);

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