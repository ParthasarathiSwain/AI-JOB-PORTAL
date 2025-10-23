package com.otz.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otz.dto.JobSeekerProfileRequest;
import com.otz.dto.JobSeekerProfileResponse;
import com.otz.service.JobSeekerProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/job-seeker/profile")
@RequiredArgsConstructor
public class JobSeekerProfileController {

    private final JobSeekerProfileService profileService;

    // Create / Update
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/{userId}")
    public ResponseEntity<JobSeekerProfileResponse> createOrUpdateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody JobSeekerProfileRequest request) {

        JobSeekerProfileResponse response = profileService.createOrUpdate(userId, request);
        return ResponseEntity.ok(response);
    }

    // Get own profile
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/{userId}")
    public ResponseEntity<JobSeekerProfileResponse> getProfile(@PathVariable Long userId) {
        JobSeekerProfileResponse response = profileService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    // Get all profiles (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<JobSeekerProfileResponse>> getAllProfiles() {
        List<JobSeekerProfileResponse> profiles = profileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    // Delete profile (JOB_SEEKER only)
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long userId) {
        profileService.deleteProfile(userId);
        return ResponseEntity.ok("Profile deleted successfully");
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER')")
    @GetMapping("/search")
    public ResponseEntity<Page<JobSeekerProfileResponse>> searchProfiles(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxSalary,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "experienceYears") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Page<JobSeekerProfileResponse> results =
                profileService.searchProfiles(location, skills, minExperience, maxSalary, page, size, sortBy, sortDir);
        return ResponseEntity.ok(results);
    }

}
