package com.vk.placeprep.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String branch;
    private Integer graduationYear;
    private String profilePictureUrl;
    private String linkedinUrl;
    private String githubUrl;
}
