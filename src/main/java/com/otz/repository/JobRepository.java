package com.otz.repository;

import com.otz.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

    // Search by location
    Page<Job> findByLocationContainingIgnoreCase(String location, Pageable pageable);

    // Search by skills
    Page<Job> findBySkillsContainingIgnoreCase(String skills, Pageable pageable);

    // Search by title
    Page<Job> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Combined search: location + skills + title
    Page<Job> findByLocationContainingIgnoreCaseAndSkillsContainingIgnoreCaseAndTitleContainingIgnoreCase(
            String location, String skills, String title, Pageable pageable
    );
}
