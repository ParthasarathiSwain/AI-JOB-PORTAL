package com.otz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerProfileRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String description;
    private String location;
    private String website;
    private Integer companySize;
    private MultipartFile logo;
}
