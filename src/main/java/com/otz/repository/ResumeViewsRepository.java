package com.otz.repository;

import com.otz.entity.Resume;
import com.otz.entity.ResumeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeViewsRepository extends JpaRepository<ResumeView, Long> {
}
