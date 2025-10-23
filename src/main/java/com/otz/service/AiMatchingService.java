package com.otz.service;

import com.otz.dto.ResumeResponse;
import com.otz.entity.Job;
import com.otz.entity.ParsedResume;
import com.otz.entity.Resume;
import com.otz.repository.JobRepository;
import com.otz.repository.ParsedResumeRepository;
import com.otz.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiMatchingService {

    private final ResumeRepository resumeRepository;
    private final ParsedResumeRepository parsedResumeRepository;
    private final JobRepository jobRepository;
    private final ResumeParsingService resumeParsingService;

    // Recommend candidates for a job based on skill match
    public List<Resume> recommendCandidatesForJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        List<String> jobSkills = Arrays.asList(job.getSkills().split(","));
        List<Resume> resumes = resumeRepository.findAll();
        List<Resume> matched = new ArrayList<>();

        for (Resume resume : resumes) {
            ParsedResume parsed = getOrParseResume(resume);

            List<String> candidateSkills = Arrays.asList(parsed.getExtractedSkills().split(","));
            List<String> matchedSkills = candidateSkills.stream()
                    .filter(jobSkills::contains)
                    .collect(Collectors.toList());

            List<String> missingSkills = jobSkills.stream()
                    .filter(s -> !candidateSkills.contains(s))
                    .collect(Collectors.toList());

            if (!matchedSkills.isEmpty()) {
                resume.setMatchedSkills(String.join(",", matchedSkills));
                resume.setMissingSkills(String.join(",", missingSkills));
                double score = ((double) matchedSkills.size() / jobSkills.size()) * 100;
                resume.setLatestAiScore(score);
                matched.add(resume);
            }
        }

        matched.sort((r1, r2) -> Double.compare(r2.getLatestAiScore(), r1.getLatestAiScore()));
        return matched;
    }

 // Recommend jobs for a candidate based on resume skills, only APPROVED jobs
    public List<Job> recommendJobsForCandidate(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        ParsedResume parsed = parsedResumeRepository.findByResume(resume)
                .orElseThrow(() -> new RuntimeException("Parsed resume not found"));

        List<String> candidateSkills = Arrays.asList(parsed.getExtractedSkills().split(","));
        List<Job> jobs = jobRepository.findAll();

        List<Job> recommended = new ArrayList<>();
        for (Job job : jobs) {
            // Only include APPROVED jobs
            if (job.getApprovalStatus() != Job.ApprovalStatus.APPROVED) continue;

            List<String> jobSkills = Arrays.asList(job.getSkills().split(","));
            boolean hasMatch = candidateSkills.stream().anyMatch(jobSkills::contains);
            if (hasMatch) recommended.add(job);
        }

        return recommended;
    }

    // Explain AI score for a given resume and job
    public ResumeResponse getExplainableAi(Long resumeId, Long jobId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        ParsedResume parsed = getOrParseResume(resume);
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        List<String> jobSkills = Arrays.asList(job.getSkills().split(","));
        List<String> candidateSkills = Arrays.asList(parsed.getExtractedSkills().split(","));

        List<String> matchedSkills = candidateSkills.stream()
                .filter(jobSkills::contains)
                .collect(Collectors.toList());

        List<String> missingSkills = jobSkills.stream()
                .filter(s -> !candidateSkills.contains(s))
                .collect(Collectors.toList());

        double score = jobSkills.isEmpty() ? 0.0 : ((double) matchedSkills.size() / jobSkills.size()) * 100;

        resume.setMatchedSkills(String.join(",", matchedSkills));
        resume.setMissingSkills(String.join(",", missingSkills));
        resume.setLatestAiScore(score);

        ResumeResponse response = new ResumeResponse();
        response.setId(resume.getId());
        response.setJobSeekerId(resume.getJobSeeker().getId());
        response.setMatchedSkills(String.join(",", matchedSkills));
        response.setMissingSkills(String.join(",", missingSkills));
        response.setLatestAiScore(score);
        response.setLatestFeedback(resume.getLatestFeedback());

        return response;
    }

    // ✅ Utility method: get ParsedResume or parse it if missing
    private ParsedResume getOrParseResume(Resume resume) {
        return parsedResumeRepository.findByResume(resume)
                .orElseGet(() -> {
                    try {
                        return resumeParsingService.parseAndSaveResume(resume);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse resume: " + resume.getId(), e);
                    }
                });
    }
}
