package com.vk.placeprep.controller;

import com.vk.placeprep.dto.ExperienceRequest;
import com.vk.placeprep.dto.ExperienceResponse;
import com.vk.placeprep.dto.ExperienceSearchCriteria;
import com.vk.placeprep.model.Experience;
import com.vk.placeprep.model.FinalVerdict;
import com.vk.placeprep.model.JobType;
import com.vk.placeprep.security.UserPrincipal;
import com.vk.placeprep.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping
    public Page<ExperienceResponse> searchExperiences(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String jobRole,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String finalVerdict,
            @RequestParam(required = false) Boolean isAnonymous,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        ExperienceSearchCriteria criteria = ExperienceSearchCriteria.builder()
                .companyName(companyName)
                .jobRole(jobRole)
                .jobType(jobType != null ? JobType.valueOf(jobType) : null)
                .finalVerdict(finalVerdict != null ? FinalVerdict.valueOf(finalVerdict) : null)
                .isAnonymous(isAnonymous)
                .userId(currentUser != null ? currentUser.getId() : null)
                .build();

        return experienceService.searchExperiences(criteria, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> getExperienceById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser != null ? currentUser.getId() : null;
        return ResponseEntity.ok(experienceService.getExperienceById(id, userId));
    }

    @PostMapping
    public ResponseEntity<ExperienceResponse> createExperience(
            @Valid @RequestBody ExperienceRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        ExperienceResponse response = experienceService.createExperience(request, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long id,
            @Valid @RequestBody ExperienceRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        ExperienceResponse response = experienceService.updateExperience(id, request, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        experienceService.deleteExperience(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<?> getExperienceSummary(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(experienceService.getExperienceSummary(id, currentUser.getId()));
    }

    @GetMapping("/topics")
    public ResponseEntity<Set<String>> getAllTopics(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(experienceService.getAllTopicsForUser(currentUser.getId()));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<ExperienceResponse>> getUserExperiences(
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        
        ExperienceSearchCriteria criteria = ExperienceSearchCriteria.builder()
                .userId(currentUser.getId())
                .finalVerdict(status != null ? FinalVerdict.valueOf(status) : null)
                .build();
                
        return ResponseEntity.ok(experienceService.searchExperiences(criteria, pageable));
    }
}