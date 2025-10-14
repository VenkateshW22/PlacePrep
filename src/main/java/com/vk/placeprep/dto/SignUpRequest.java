package com.vk.placeprep.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private String universityEmail;
    private String password;
    private String branch;
    private int graduationYear;
}
