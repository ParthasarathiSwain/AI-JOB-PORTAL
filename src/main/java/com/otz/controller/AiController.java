package com.otz.controller;

import com.otz.dto.ResumeResponse;
import com.otz.entity.Job;
import com.otz.entity.Resume;
import com.otz.service.AiMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiMatchingService aiMatchingService;

    // ✅ Recommend candidates for a job - Only Employer
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/recommend-candidates/{jobId}")
    public ResponseEntity<List<ResumeResponse>> recommendCandidates(@PathVariable Long jobId) {
        List<Resume> resumes = aiMatchingService.recommendCandidatesForJob(jobId);

        List<ResumeResponse> response = resumes.stream().map(r -> {
            ResumeResponse resp = new ResumeResponse();
            resp.setId(r.getId());
            resp.setJobSeekerId(r.getJobSeeker().getId());
            resp.setLatestAiScore(r.getLatestAiScore());
            resp.setMatchedSkills(r.getMatchedSkills());
            resp.setMissingSkills(r.getMissingSkills());
            return resp;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ✅ Recommend jobs for a candidate - Only Job Seeker
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/recommend-jobs/{resumeId}")
    public ResponseEntity<List<Job>> recommendJobs(@PathVariable Long resumeId) {
        List<Job> jobs = aiMatchingService.recommendJobsForCandidate(resumeId);
        return ResponseEntity.ok(jobs);
    }

    // ✅ Explain AI Score - Only Employer
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/explain/{resumeId}/{jobId}")
    public ResponseEntity<ResumeResponse> explainAiScore(
            @PathVariable Long resumeId,
            @PathVariable Long jobId) {
        ResumeResponse resp = aiMatchingService.getExplainableAi(resumeId, jobId);
        return ResponseEntity.ok(resp);
    }
}
