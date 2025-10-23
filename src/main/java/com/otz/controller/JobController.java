package com.otz.controller;

import com.otz.dto.JobRequest;
import com.otz.dto.JobResponse;
import com.otz.entity.Job;
import com.otz.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // Create / Update Job with banner
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/{employerId}")
    public ResponseEntity<JobResponse> createJob(
            @PathVariable Long employerId,
            @Valid @ModelAttribute JobRequest request,
            @RequestParam(required = false) Long jobId) throws IOException {
        return ResponseEntity.ok(jobService.createOrUpdateJob(employerId, request, jobId));
    }

    // Get Job Details
    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getJob(jobId));
    }

    // Delete Job
    @PreAuthorize("hasRole('EMPLOYER')")
    @DeleteMapping("/{jobId}/{employerId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId, @PathVariable Long employerId) {
        jobService.deleteJob(jobId, employerId);
        return ResponseEntity.ok("Job deleted successfully");
    }

    // List all jobs
    @GetMapping("/all")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // Search jobs
    @GetMapping("/search")
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(jobService.searchJobs(location, skills, title, page, size, sortBy, sortDir));
    }

    // Admin approve/reject
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{jobId}/approval")
    public ResponseEntity<JobResponse> updateApprovalStatus(
            @PathVariable Long jobId,
            @RequestParam Job.ApprovalStatus status,
            @RequestParam Long adminId) {
        return ResponseEntity.ok(jobService.updateApprovalStatus(jobId, status, adminId));
    }
}
