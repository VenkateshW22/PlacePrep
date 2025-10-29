package com.vk.placeprep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP for PlacePrep Authentication");
        message.setText("Your OTP for authentication is: " + otp + "\n\n" +
                "This OTP will expire in 5 minutes.\n" +
                "If you didn't request this OTP, please ignore this email.");

        mailSender.send(message);
    }
}