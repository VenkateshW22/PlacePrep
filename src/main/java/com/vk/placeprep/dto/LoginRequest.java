package com.vk.placeprep.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String universityEmail;
    private String password;
}
