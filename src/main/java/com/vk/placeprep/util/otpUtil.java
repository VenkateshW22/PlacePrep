package com.vk.placeprep.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Component
public class otpUtil {

    private static final long OTP_VALID_DURATION = 5; // 5 minutes
    private final SecureRandom random = new SecureRandom();

    //  store for OTPs
    private final Map<String, OtpData> otpStore = new HashMap<>();

    // Inner class to store OTP details
    private static class OtpData {
        String otp;
        LocalDateTime generationTime;

        OtpData(String otp, LocalDateTime generationTime) {
            this.otp = otp;
            this.generationTime = generationTime;
        }
    }

    // Generate and store OTP for a user (email or username)
    public String generateOtp(String identifier) {
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStore.put(identifier, new OtpData(otp, LocalDateTime.now()));
        return otp;
    }

    // Verify OTP for a given user (and check expiry)
    public boolean verifyOtp(String identifier, String inputOtp) {
        OtpData data = otpStore.get(identifier);

        if (data == null) {
            return false; // No OTP generated for this user
        }

        if (isOtpExpired(data.generationTime)) {
            otpStore.remove(identifier);
            return false; // OTP expired
        }

        boolean isValid = data.otp.equals(inputOtp);
        if (isValid) {
            otpStore.remove(identifier); // OTP can be used only once
        }

        return isValid;
    }

    // Check if OTP is expired
    private boolean isOtpExpired(LocalDateTime generationTime) {
        return LocalDateTime.now().isAfter(generationTime.plusMinutes(OTP_VALID_DURATION));
    }
}
