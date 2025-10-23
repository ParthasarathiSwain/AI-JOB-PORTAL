package com.otz.repository;

import com.otz.entity.JobApplication;
import com.otz.entity.JobApplication.ApplicationStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
	List<JobApplication> findByJobId(Long jobId);

    List<JobApplication> findByCandidateId(Long candidateId);

    List<JobApplication> findByJobIdAndStatus(Long jobId, ApplicationStatus status);
}
