package com.otz.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobApplicationResponse {
    private Long id;
    private Long candidateId;
    private Long jobId;
    private String resumePath;
    private String coverLetter;
    private String status;
    private LocalDateTime appliedDate;
    private LocalDateTime updatedDate;
    private Long updatedBy;
}
