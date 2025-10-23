package com.otz.dto;

import com.otz.entity.EmployerProfile.ApprovalStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployerProfileResponse {
    private Long id;
    private Long userId;
    private String companyName;
    private String website;
    private String location;
    private String description;
    private String logoPath;
    private ApprovalStatus approvalStatus;
    private Integer views;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long approvedBy;
}
