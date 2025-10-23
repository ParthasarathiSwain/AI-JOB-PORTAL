package com.otz.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiScoreDTO {
    private Long resumeId;
    private Long jobId;
    private Double matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
}
