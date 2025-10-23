package com.otz.service;

import com.otz.dto.JobRequest;
import com.otz.dto.JobResponse;
import com.otz.entity.Job;
import com.otz.entity.User;
import com.otz.repository.JobRepository;
import com.otz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    private final String uploadDir = "C:/Users/PC/SpringBoot-WorkSpace/Ai-Job-Portal/uploads/jobBanner";

    // Create / Update Job
    public JobResponse createOrUpdateJob(Long employerId, JobRequest request, Long jobId) throws IOException {
        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        Job job;
        if (jobId != null) {
            job = jobRepository.findById(jobId)
                    .orElseThrow(() -> new RuntimeException("Job not found"));
        } else {
            job = new Job();
            job.setEmployer(employer);
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSkills(request.getSkills());
        job.setMinExperience(request.getMinExperience());
        job.setMaxSalary(request.getMaxSalary());

        // Banner upload
        MultipartFile file = request.getBanner();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Ensure upload directory exists
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        // Use File.separator for cross-platform path building
        File destinationFile = new File(uploadPath, fileName);
        file.transferTo(destinationFile);
        

        Job saved = jobRepository.save(job);
        return mapToResponse(saved);
    }

    // Get Job Details
    public JobResponse getJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setViews(job.getViews() + 1);
        jobRepository.save(job);
        return mapToResponse(job);
    }

    // Delete Job
    public void deleteJob(Long jobId, Long employerId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        jobRepository.delete(job);
    }

    // List all jobs
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Search jobs with filters, pagination, sorting
    public Page<JobResponse> searchJobs(String location, String skills, String title,
                                        int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Page<Job> jobs;
        if (location != null && skills != null && title != null) {
            jobs = jobRepository.findByLocationContainingIgnoreCaseAndSkillsContainingIgnoreCaseAndTitleContainingIgnoreCase(
                    location, skills, title, pageable);
        } else if (location != null) {
            jobs = jobRepository.findByLocationContainingIgnoreCase(location, pageable);
        } else if (skills != null) {
            jobs = jobRepository.findBySkillsContainingIgnoreCase(skills, pageable);
        } else if (title != null) {
            jobs = jobRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else {
            jobs = jobRepository.findAll(pageable);
        }

        return jobs.map(this::mapToResponse);
    }

    // Admin: Approve / Reject job
    public JobResponse updateApprovalStatus(Long jobId, Job.ApprovalStatus status, Long adminId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        job.setApprovalStatus(status);
        job.setApprovedBy(admin);
        Job saved = jobRepository.save(job);
        return mapToResponse(saved);
    }

    private JobResponse mapToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setEmployerId(job.getEmployer().getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setLocation(job.getLocation());
        response.setSkills(job.getSkills());
        response.setMinExperience(job.getMinExperience());
        response.setMaxSalary(job.getMaxSalary());
        response.setBannerPath(job.getBannerPath());
        response.setApprovalStatus(job.getApprovalStatus().name());
        response.setViews(job.getViews());
        response.setCreatedDate(job.getCreatedDate());
        response.setUpdatedDate(job.getUpdatedDate());
        response.setApprovedBy(job.getApprovedBy() != null ? job.getApprovedBy().getId() : null);
        return response;
    }
}
