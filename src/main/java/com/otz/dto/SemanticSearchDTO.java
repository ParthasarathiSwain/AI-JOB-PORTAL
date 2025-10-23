package com.otz.dto;

import lombok.Data;

import java.util.List;

@Data
public class SemanticSearchDTO {
    private Long referenceId; // resumeId/jobId
    private String referenceType; // "RESUME" or "JOB"
    private List<Long> matchedIds; // matched jobIds or resumeIds
    private List<Double> similarityScores;
}
