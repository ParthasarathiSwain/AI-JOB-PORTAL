package com.otz.repository;

import com.otz.entity.ParsedResume;
import com.otz.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParsedResumeRepository extends JpaRepository<ParsedResume, Long> {
    Optional<ParsedResume> findByResume(Resume resume);
}
