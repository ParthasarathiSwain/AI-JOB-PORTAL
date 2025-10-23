package com.otz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otz.entity.AiScoreHistory;

@Repository
public interface AiScoreHistoryRepository extends JpaRepository<AiScoreHistory, Long> {
	 List<AiScoreHistory> findByResumeId(Long resumeId);
}
