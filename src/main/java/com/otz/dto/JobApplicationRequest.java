package com.otz.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class JobApplicationRequest {
    private MultipartFile resume;
    private String coverLetter;
}
