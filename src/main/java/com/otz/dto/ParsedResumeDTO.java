package com.otz.dto;

import lombok.Data;

@Data
public class ParsedResumeDTO {
    private Long resumeId;
    private String extractedSkills;
    private String extractedExperience;
    private String extractedEducation;
}
