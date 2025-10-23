package com.otz.dto;

import lombok.Data;

import java.util.List;

@Data
public class SkillNormalizationDTO {
    private Long resumeId;
    private List<String> originalSkills;
    private List<String> normalizedSkills;
}
