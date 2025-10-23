package com.otz.controller;

import com.otz.dto.JobApplicationRequest;
import com.otz.dto.JobApplicationResponse;
import com.otz.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService service;

    // Candidate applies
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/apply")
    public ResponseEntity<JobApplicationResponse> applyToJob(
            @RequestParam Long jobId,
            @RequestParam Long candidateId,
            @ModelAttribute JobApplicationRequest request) throws IOException {
        return ResponseEntity.ok(service.applyToJob(jobId, candidateId, request));
    }

    // Employer views applications
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<JobApplicationResponse>> getApplicationsForJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(service.getApplicationsForJob(jobId));
    }

    // Update application status
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<JobApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updateApplicationStatus(applicationId, status));
    }
    // Candidate: view their applications
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<JobApplicationResponse>> getCandidateApplications(@PathVariable Long candidateId) {
        List<JobApplicationResponse> responses = service.getApplicationsByCandidate(candidateId);
        return ResponseEntity.ok(responses);
    }
    // Export applications CSV
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @GetMapping("/job/{jobId}/export")
    public ResponseEntity<String> exportApplicationsCSV(@PathVariable Long jobId) throws IOException {
        String filePath = service.exportApplicationsToCSV(jobId);
        return ResponseEntity.ok("CSV exported successfully: " + filePath);
    }
}
