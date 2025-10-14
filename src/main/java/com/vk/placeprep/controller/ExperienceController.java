package com.vk.placeprep.controller;

import com.vk.placeprep.dto.ExperienceRequest;
import com.vk.placeprep.dto.ExperienceResponse;
import com.vk.placeprep.model.Experience;
import com.vk.placeprep.security.UserPrincipal;
import com.vk.placeprep.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {

    @Autowired
    private ExperienceService experienceService;

    @GetMapping
    public Page<ExperienceResponse> getExperiences(Pageable pageable) {
        return experienceService.getAllExperiences(pageable)
                .map(ExperienceResponse::fromEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> getExperienceById(@PathVariable Long id) {
        Experience experience = experienceService.getExperienceById(id);
        return ResponseEntity.ok(ExperienceResponse.fromEntity(experience));
    }

    @PostMapping
    public ResponseEntity<ExperienceResponse> createExperience(
            @RequestBody ExperienceRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Experience createdExperience = experienceService.createExperience(request, currentUser.getId());
        return ResponseEntity.ok(ExperienceResponse.fromEntity(createdExperience));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long id,
            @RequestBody ExperienceRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Experience updatedExperience = experienceService.updateExperience(id, request, currentUser.getId());
        return ResponseEntity.ok(ExperienceResponse.fromEntity(updatedExperience));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        experienceService.deleteExperience(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}