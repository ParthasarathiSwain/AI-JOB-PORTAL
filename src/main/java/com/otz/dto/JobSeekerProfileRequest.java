package com.otz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobSeekerProfileRequest {
    @NotBlank
    private String phone;
    private String location;
    private String skills;
    private Integer experienceYears;
    private Integer salary;
}
