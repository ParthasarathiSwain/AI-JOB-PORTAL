package com.otz.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobResponse {
    private Long id;
    private Long employerId;
    private String title;
    private String description;
    private String location;
    private String skills;
    private Integer minExperience;
    private Integer maxSalary;
    private String bannerPath; 
    private String approvalStatus;
    private Integer views;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long approvedBy;
}
