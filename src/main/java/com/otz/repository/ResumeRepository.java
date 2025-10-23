package com.otz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.otz.entity.JobSeekerProfile;
import com.otz.entity.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByJobSeeker(JobSeekerProfile jobSeeker);
    @Query("SELECT r FROM Resume r LEFT JOIN FETCH r.parsedResume")
    List<Resume> findAllWithParsedResume();
}
