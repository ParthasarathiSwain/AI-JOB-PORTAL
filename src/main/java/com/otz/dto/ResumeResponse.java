package com.otz.dto;

import com.otz.entity.ResumePrivacy;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResumeResponse {
    private Long id;
    private Long jobSeekerId;
    private String filePath;
    private LocalDateTime uploadedAt;
    private ResumePrivacy privacy;
    private Integer totalViews;
    private Double latestAiScore;
    private String latestFeedback;

    // For explainable AI
    private String matchedSkills;
    private String missingSkills;
}
