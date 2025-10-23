package com.otz.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.otz.dto.ResumeRequest;
import com.otz.dto.ResumeResponse;
import com.otz.entity.*;
import com.otz.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final ResumeViewsRepository resumeViewsRepository;
    private final AiScoreHistoryRepository aiScoreHistoryRepository;
    private final ReviewsRepository reviewsRepository;
    private final ResumeParsingService resumeParsingService;
    private final OpenAiClientService openAiClientService;

    private final String uploadDir = "C:/Users/PC/SpringBoot-WorkSpace/Ai-Job-Portal/uploads/resumes";

    // Upload Resume
    public ResumeResponse uploadResume(Long jobSeekerId, ResumeRequest request) throws IOException {
        JobSeekerProfile jobSeeker = jobSeekerProfileRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job seeker not found"));

        MultipartFile file = request.getFile();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) uploadPath.mkdirs();

        File destinationFile = new File(uploadPath, fileName);
        file.transferTo(destinationFile);

        Resume resume = new Resume();
        resume.setJobSeeker(jobSeeker);
        resume.setFilePath(destinationFile.getAbsolutePath());
        resume.setPrivacy(request.getPrivacy() != null ? request.getPrivacy() : ResumePrivacy.PRIVATE);
        resume.setUploadedAt(LocalDateTime.now());

        Resume saved = resumeRepository.save(resume);
        resumeParsingService.parseAndSaveResume(saved);

        return mapToResponse(saved);
    }

    // Generate AI Summary for a Resume
    public String generateResumeSummary(Long resumeId) throws IOException {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        ParsedResume parsed = resumeParsingService.parseAndSaveResume(resume);
        String resumeText = String.join(",",
                parsed.getExtractedSkills(),
                parsed.getExtractedEducation(),
                parsed.getExtractedExperience()
        );

        return openAiClientService.generateSummary(resumeText);
    }

    // Compute Semantic Match with Job Description
    public double computeSemanticMatch(Long resumeId, String jobDescription) throws IOException {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        ParsedResume parsed = resumeParsingService.parseAndSaveResume(resume);
        String resumeText = String.join(",",
                parsed.getExtractedSkills(),
                parsed.getExtractedEducation(),
                parsed.getExtractedExperience()
        );

        return openAiClientService.computeSemanticMatch(resumeText, jobDescription);
    }

    // Get all resumes for a job seeker
    public List<ResumeResponse> getResumes(Long jobSeekerId) {
        JobSeekerProfile jobSeeker = jobSeekerProfileRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Job seeker not found"));

        return resumeRepository.findByJobSeeker(jobSeeker)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Delete Resume
    public void deleteResume(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        File file = new File(resume.getFilePath());
        if (file.exists()) file.delete();

        resumeRepository.delete(resume);
    }

    // Update Privacy
    public ResumeResponse updatePrivacy(Long resumeId, ResumePrivacy privacy) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        resume.setPrivacy(privacy);
        resumeRepository.save(resume);
        return mapToResponse(resume);
    }

    // Increment Resume View
    public ResumeResponse incrementView(Long resumeId, Long employerId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        resume.setTotalViews(resume.getTotalViews() + 1);
        resumeRepository.save(resume);

        ResumeView view = new ResumeView();
        view.setResume(resume);
        view.setViewedBy(employerId);
        view.setViewDate(LocalDateTime.now());
        resumeViewsRepository.save(view);

        return mapToResponse(resume);
    }

    // Add AI Score
    public ResumeResponse addAiScore(Long resumeId, Double score) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        resume.setLatestAiScore(score);
        resumeRepository.save(resume);

        AiScoreHistory history = new AiScoreHistory();
        history.setResume(resume);
        history.setMatchScore(score);
        history.setAnalysisDate(LocalDateTime.now());
        aiScoreHistoryRepository.save(history);

        return mapToResponse(resume);
    }

    // Add Feedback/Review
    public ResumeResponse addFeedback(Long resumeId, Long employerId, String comment, Integer rating) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Review review = new Review();
        review.setResume(resume);
        review.setEmployerId(employerId);
        review.setJobSeekerId(resume.getJobSeeker().getId());
        review.setComment(comment);
        review.setRating(rating != null ? rating : 5); // default 5
        reviewsRepository.save(review);

        resume.setLatestFeedback(comment);
        resumeRepository.save(resume);

        return mapToResponse(resume);
    }

    // Map Resume entity to DTO
    private ResumeResponse mapToResponse(Resume resume) {
        ResumeResponse response = new ResumeResponse();
        response.setId(resume.getId());
        response.setJobSeekerId(resume.getJobSeeker().getId());
        response.setFilePath(resume.getFilePath());
        response.setUploadedAt(resume.getUploadedAt());
        response.setPrivacy(resume.getPrivacy());
        response.setTotalViews(resume.getTotalViews());
        response.setLatestAiScore(resume.getLatestAiScore());
        response.setLatestFeedback(resume.getLatestFeedback());
        return response;
    }
}
