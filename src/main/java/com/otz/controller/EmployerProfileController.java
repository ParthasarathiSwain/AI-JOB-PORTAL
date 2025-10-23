package com.otz.controller;

import com.otz.dto.EmployerProfileRequest;
import com.otz.dto.EmployerProfileResponse;
import com.otz.entity.EmployerProfile;
import com.otz.service.EmployerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employer/profile")
@RequiredArgsConstructor
public class EmployerProfileController {

    private final EmployerProfileService service;

    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/{userId}")
    public ResponseEntity<EmployerProfileResponse> createOrUpdateProfile(
            @PathVariable Long userId,
            @ModelAttribute EmployerProfileRequest request) throws IOException {
        return ResponseEntity.ok(service.createOrUpdateProfile(userId, request));
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/{userId}")
    public ResponseEntity<EmployerProfileResponse> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getProfile(userId));
    }

    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> deleteProfile(@PathVariable Long userId) {
        service.deleteProfile(userId);
        Map<String, String> res = new HashMap<>();
        res.put("message", "Employer profile deleted successfully");
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('JOB_SEEKER')")
    @GetMapping("/all")
    public ResponseEntity<List<EmployerProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(service.getAllProfiles());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('JOB_SEEKER')")
    @GetMapping("/search")
    public ResponseEntity<?> searchProfiles(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<EmployerProfileResponse> profiles = service.searchProfiles(companyName, location, page, size);
        if (profiles.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", false);
            response.put("message", "No employer profiles found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(profiles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{profileId}/approval")
    public ResponseEntity<EmployerProfileResponse> updateApprovalStatus(
            @PathVariable Long profileId,
            @RequestParam EmployerProfile.ApprovalStatus status,
            @RequestParam Long adminId) {
        return ResponseEntity.ok(service.updateApprovalStatus(profileId, status, adminId));
    }
}
