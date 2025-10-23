package com.otz.dto;

import lombok.Data;

@Data
public class JobSeekerProfileResponse {
    private Long id;
    private Long userId;
    private String phone;
    private String location;
    private String skills;
    private Integer experienceYears;
    private Integer salary;
}
