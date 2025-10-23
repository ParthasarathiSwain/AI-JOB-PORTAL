package com.otz.controller;
import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.otz.dto.ResumeRequest;
import com.otz.dto.ResumeResponse;
import com.otz.entity.ResumePrivacy;
import com.otz.service.ResumeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    // Only Job Seeker can upload
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/upload/{jobSeekerId}")
    public ResponseEntity<ResumeResponse> uploadResume(
            @PathVariable Long jobSeekerId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "privacy", required = false) ResumePrivacy privacy
    ) throws IOException {
        ResumeRequest request = new ResumeRequest();
        request.setFile(file);
        request.setPrivacy(privacy);
        return ResponseEntity.ok(resumeService.uploadResume(jobSeekerId, request));
    }

    // Job Seeker can view their resumes
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/jobSeeker/{jobSeekerId}")
    public ResponseEntity<List<ResumeResponse>> getResumes(@PathVariable Long jobSeekerId) {
        return ResponseEntity.ok(resumeService.getResumes(jobSeekerId));
    }

    // Job Seeker can delete their resume
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @DeleteMapping("/{resumeId}")
    public ResponseEntity<String> deleteResume(@PathVariable Long resumeId) {
        resumeService.deleteResume(resumeId);
        return ResponseEntity.ok("Resume deleted successfully");
    }

    // Job Seeker can update privacy
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PutMapping("/{resumeId}/privacy")
    public ResponseEntity<ResumeResponse> updatePrivacy(
            @PathVariable Long resumeId,
            @RequestParam ResumePrivacy privacy
    ) {
        return ResponseEntity.ok(resumeService.updatePrivacy(resumeId, privacy));
    }

    // Employer can increment view
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/{resumeId}/view/{employerId}")
    public ResponseEntity<ResumeResponse> incrementView(
            @PathVariable Long resumeId,
            @PathVariable Long employerId
    ) {
        return ResponseEntity.ok(resumeService.incrementView(resumeId, employerId));
    }

    // Job Seeker can generate AI summary
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/{resumeId}/summary")
    public ResponseEntity<String> generateSummary(@PathVariable Long resumeId) throws IOException {
        String summary = resumeService.generateResumeSummary(resumeId);
        return ResponseEntity.ok(summary);
    }

    // Job Seeker can compute semantic match
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/{resumeId}/semantic-match")
    public ResponseEntity<Double> computeSemanticMatch(
            @PathVariable Long resumeId,
            @RequestParam String jobDescription
    ) throws IOException {
        double score = resumeService.computeSemanticMatch(resumeId, jobDescription);
        return ResponseEntity.ok(score);
    }

    // Employer can add AI score
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/{resumeId}/ai-score")
    public ResponseEntity<ResumeResponse> addAiScore(
            @PathVariable Long resumeId,
            @RequestParam Double score
    ) {
        return ResponseEntity.ok(resumeService.addAiScore(resumeId, score));
    }

    // Employer can add feedback
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/{resumeId}/feedback/{employerId}")
    public ResponseEntity<ResumeResponse> addFeedback(
            @PathVariable Long resumeId,
            @PathVariable Long employerId,
            @RequestParam String comment,
            @RequestParam(required = false) Integer rating
    ) {
        return ResponseEntity.ok(resumeService.addFeedback(resumeId, employerId, comment, rating));
    }
}
