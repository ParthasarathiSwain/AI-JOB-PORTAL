package com.otz.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.otz.dto.JobApplicationRequest;
import com.otz.dto.JobApplicationResponse;
import com.otz.entity.Job;
import com.otz.entity.JobApplication;
import com.otz.entity.User;
import com.otz.repository.JobApplicationRepository;
import com.otz.repository.JobRepository;
import com.otz.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    private final String uploadDir = "C:/Users/PC/SpringBoot-WorkSpace/Ai-Job-Portal/uploads/job-applications/";

    // Candidate applies to job
    public JobApplicationResponse applyToJob(Long jobId, Long candidateId, JobApplicationRequest request) throws IOException {
        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobApplication application = JobApplication.builder()
                .candidate(candidate)
                .job(job)
                .status(JobApplication.ApplicationStatus.APPLIED)
                .coverLetter(request.getCoverLetter())
                .build();

        MultipartFile resume = request.getResume();
        if (resume != null && !resume.isEmpty()) {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String fileName = System.currentTimeMillis() + "_" + resume.getOriginalFilename();
            resume.transferTo(new File(uploadDir + fileName));
            application.setResumePath(fileName);
        }

        JobApplication saved = applicationRepository.save(application);
        return mapToResponse(saved);
    }

    // Get applications for a job
    public List<JobApplicationResponse> getApplicationsForJob(Long jobId) {
        List<JobApplication> applications = applicationRepository.findByJobId(jobId);
        return applications.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Update application status
    public JobApplicationResponse updateApplicationStatus(Long applicationId, String status) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(JobApplication.ApplicationStatus.valueOf(status.toUpperCase()));

        JobApplication updated = applicationRepository.save(application);
        return mapToResponse(updated);
    }
 // Export applications to CSV
    public String exportApplicationsToCSV(Long jobId) throws IOException {
        List<JobApplication> applications = applicationRepository.findByJobId(jobId);

        String csvFile = "C:/Users/PC/SpringBoot-WorkSpace/Ai-Job-Portal/uploads/job-applications/job_" + jobId + "_applications.csv";
        FileWriter writer = new FileWriter(csvFile);
        writer.append("ApplicationId,CandidateId,JobId,Status,AppliedDate,UpdatedDate,ResumePath,CoverLetter\n");

        for (JobApplication app : applications) {
            writer.append(app.getId() + "," +
                    app.getCandidate().getId() + "," +
                    app.getJob().getId() + "," +
                    app.getStatus() + "," +
                    app.getAppliedDate() + "," +
                    app.getUpdatedDate() + "," +
                    app.getResumePath() + "," +
                    (app.getCoverLetter() != null ? app.getCoverLetter().replace(",", ";") : "") + "\n");
        }

        writer.flush();
        writer.close();
        return csvFile;
    }
    public List<JobApplicationResponse> getApplicationsByCandidate(Long candidateId) {
        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        List<JobApplication> applications = applicationRepository.findByCandidateId(candidateId);

        return applications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    private JobApplicationResponse mapToResponse(JobApplication application) {
        JobApplicationResponse response = new JobApplicationResponse();
        response.setId(application.getId());
        response.setCandidateId(application.getCandidate().getId());
        response.setJobId(application.getJob().getId());
        response.setResumePath(application.getResumePath());
        response.setCoverLetter(application.getCoverLetter());
        response.setStatus(application.getStatus().name());
        response.setAppliedDate(application.getAppliedDate());
        response.setUpdatedDate(application.getUpdatedDate());
        response.setUpdatedBy(application.getUpdatedBy() != null ? application.getUpdatedBy().getId() : null);
        return response;
    }
}
