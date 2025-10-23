package com.otz.dto;

import lombok.Data;

import java.util.List;

@Data
public class JobRecommendationDTO {
    private Long referenceId; // candidateId or jobId
    private String referenceType; // "CANDIDATE" or "JOB"
    private List<Long> recommendedIds; // jobIds or candidateIds
    private List<Double> recommendationScores;
}
