package com.otz.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Data
public class JobRequest {
    @NotBlank
    private String title;
    private String description;
    private String location;
    private String skills;
    private Integer minExperience;
    private Integer maxSalary;
    private MultipartFile banner;
}
